@echo off
title Monitor GRU - Sistema Autônomo
chcp 65001 > nul
cls

echo ======================================================
echo    SISTEMA DE GESTAO AEROPORTUARIA - AUTO REFRESH
echo ======================================================
echo.

echo [1/2] Compilando arquivos...
javac -encoding UTF-8 *.java

echo [2/2] Iniciando Monitor GUI...
echo (A janela se atualizara sozinha a cada 60 segundos)
java MonitorGUI

pause