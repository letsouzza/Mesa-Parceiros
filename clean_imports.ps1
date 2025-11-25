$filePath = "C:\Users\subli\StudioProjects\Mesa-Parceiros\app\src\main\java\br\senai\sp\jandira\mesaparceiros\service\EmpresaService.kt"
$content = Get-Content $filePath

# Remove imports duplicados
$content = $content | Where-Object { $_ -ne "import br.senai.sp.jandira.mesaparceiros.model.EmpresaUpdate" -or $content.IndexOf($_) -eq [array]::IndexOf($content, $_) }
$content = $content | Where-Object { $_ -ne "import br.senai.sp.jandira.mesaparceiros.model.ResponseGeral" -or $content.IndexOf($_) -eq [array]::IndexOf($content, $_) }
$content = $content | Where-Object { $_ -ne "import retrofit2.http.PUT" -or $content.IndexOf($_) -eq [array]::IndexOf($content, $_) }

Set-Content $filePath -Value $content
