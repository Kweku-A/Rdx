param(
    [string]$mainBranch = "develop",
    [bool]$enableLogs = $true
)

# --- Temp folders ---
$TMP_DIR = "coverage_tmp"
$MAIN_DIR = Join-Path $TMP_DIR "main"
$FEATURE_DIR = Join-Path $TMP_DIR "feature"
$HTML_FILE = Join-Path $TMP_DIR "coverage_report.html"

New-Item -ItemType Directory -Force -Path $MAIN_DIR, $FEATURE_DIR | Out-Null

function Log($msg)
{
    if ($enableLogs)
    {
        Write-Host ("[{0}] {1}" -f (Get-Date -Format "HH:mm:ss"), $msg)
    }
}

function Run-Command($cmd)
{
    Log "Running command: $cmd"
    Invoke-Expression $cmd
}

function Run-Coverage($branch, $outDir)
{
    Write-Host "`n=============================="
    Write-Host "Switching to branch: $branch"
    Write-Host "=============================="
    Run-Command "git checkout $branch -q"

    Write-Host "Running tests and generating coverage..."
    Run-Command ".\gradlew clean testGhs_ukDebugUnitTest testGhs_ukDebugUnitTestCoverage --no-daemon --parallel --max-workers=8 --build-cache --console=plain"

    Write-Host "Collecting XML reports..."
    Get-ChildItem -Recurse -Directory -Filter "build" | ForEach-Object {
        $xmlDir = Join-Path $_.FullName "reports\jacoco\xml"
        if (Test-Path $xmlDir)
        {
            $xmlFile = Get-ChildItem $xmlDir -Filter "*.xml" | Select-Object -First 1
            $safeName = ($_ | Split-Path -Leaf) -replace '[^a-zA-Z0-9_]', '_'
            $targetFile = Join-Path $outDir "$safeName.xml"
            if ($xmlFile)
            {
                Copy-Item $xmlFile.FullName -Destination $targetFile -Force
            }
            else
            {
                "<report></report>" | Out-File -Encoding UTF8 $targetFile
            }
        }
    }
}

function Parse-Coverage($file)
{
    if (-not (Test-Path $file) -or ((Get-Content $file -Raw).Trim() -eq ""))
    {
        return @(0, 0, 0)
    }
    try
    {
        [xml]$xml = Get-Content $file -Raw
        $lc = [double]($xml.report.counter | Where-Object { $_.type -eq "LINE" }).covered
        $lm = [double]($xml.report.counter | Where-Object { $_.type -eq "LINE" }).missed
        $bc = [double]($xml.report.counter | Where-Object { $_.type -eq "BRANCH" }).covered
        $bm = [double]($xml.report.counter | Where-Object { $_.type -eq "BRANCH" }).missed
        $ic = [double]($xml.report.counter | Where-Object { $_.type -eq "INSTRUCTION" }).covered
        $im = [double]($xml.report.counter | Where-Object { $_.type -eq "INSTRUCTION" }).missed

        $linePct = if (($lc + $lm) -eq 0)
        {
            0
        }
        else
        {
            [math]::Round($lc*100/($lc + $lm), 2)
        }
        $branchPct = if (($bc + $bm) -eq 0)
        {
            0
        }
        else
        {
            [math]::Round($bc*100/($bc + $bm), 2)
        }
        $instrPct = if (($ic + $im) -eq 0)
        {
            0
        }
        else
        {
            [math]::Round($ic*100/($ic + $im), 2)
        }

        return @($linePct, $branchPct, $instrPct)
    }
    catch
    {
        return @(0, 0, 0)
    }
}

# --- Detect branches ---
$CURRENT_BRANCH = (git rev-parse --abbrev-ref HEAD).Trim()
Log "Current branch: $CURRENT_BRANCH"
Log "Main branch: $mainBranch"

# --- Run coverage ---
Run-Coverage $mainBranch $MAIN_DIR
Run-Command "git checkout $CURRENT_BRANCH -q"
Run-Coverage $CURRENT_BRANCH $FEATURE_DIR

# --- Generate HTML safely ---
$sb = New-Object System.Text.StringBuilder
$null = $sb.AppendLine("<html><head><meta charset='UTF-8'><title>Coverage Comparison</title>")
$null = $sb.AppendLine("<style>")
$null = $sb.AppendLine("body{font-family:Arial;margin:20px;}")
$null = $sb.AppendLine("table{border-collapse:collapse;width:100%;margin-bottom:20px;}")
$null = $sb.AppendLine("th,td{border:1px solid #ccc;padding:6px;text-align:center;}")
$null = $sb.AppendLine(".up{color:green;font-weight:bold;}")
$null = $sb.AppendLine(".down{color:red;font-weight:bold;}")
$null = $sb.AppendLine(".same{color:black;}")
$null = $sb.AppendLine("</style></head><body>")
$null = $sb.AppendLine("<h2>Module Coverage Comparison</h2>")
$null = $sb.AppendLine("<table>")
$null = $sb.AppendLine("<tr><th>Module</th><th>Metric</th><th>Main</th><th>Feature</th></tr>")

function cls($a, $b)
{
    if ($b -gt $a)
    {
        "up"
    }
    elseif ($b -lt $a)
    {
        "down"
    }
    else
    {
        "same"
    }
}

Get-ChildItem $MAIN_DIR -Filter "*.xml" | ForEach-Object {
    $module = $_.BaseName
    $mainCov = Parse-Coverage $_.FullName
    $featCov = Parse-Coverage (Join-Path $FEATURE_DIR "$module.xml")

    $null = $sb.AppendLine("<tr><td rowspan='3'>$module</td><td>Line</td><td>$( $mainCov[0] )</td><td class='$( cls $mainCov[0] $featCov[0] )'>$( $featCov[0] )</td></tr>")
    $null = $sb.AppendLine("<tr><td>Branch</td><td>$( $mainCov[1] )</td><td class='$( cls $mainCov[1] $featCov[1] )'>$( $featCov[1] )</td></tr>")
    $null = $sb.AppendLine("<tr><td>Overall</td><td>$( $mainCov[2] )</td><td class='$( cls $mainCov[2] $featCov[2] )'>$( $featCov[2] )</td></tr>")
}

$null = $sb.AppendLine("</table></body></html>")
$sb.ToString() | Out-File -Encoding UTF8 $HTML_FILE

Log "HTML coverage report generated at: $HTML_FILE"
Start-Process $HTML_FILE
