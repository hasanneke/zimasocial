#!/bin/bash
mvn clean package
scp "$(find target -maxdepth 1 -type f -name '*.jar' ! -name '*.jar.original' | head -n 1)" root@80.241.210.18:/opt/zima/