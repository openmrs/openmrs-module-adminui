call mvn clean install -DskipTests
copy "C:\Users\ujjwal\Github\adminui\omod\target\adminui-1.0-SNAPSHOT.omod" "C:\Users\ujjwal\Application Data\OpenMRS\modules\adminui-1.0-SNAPSHOT.omod" /y
cd ..
cd openmrs-core/webapp
call mvn jetty:run -Djetty.port=9999 -DuiFramework.development.adminui="C:\Users\ujjwal\Github\adminui"
pause