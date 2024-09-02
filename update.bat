rem update tiles ----------------------------------------------
curl -L -O https://github.com/lancard/tile-server/archive/refs/heads/master.zip
.\7za.exe x master.zip
move .\tile-server-master\korea-openstreetmap-tiles .\tile-server-master\tiles
rd /S /Q .\app\src\main\assets\www\tiles
move .\tile-server-master\tiles .\app\src\main\assets\www
rd /S /Q tile-server-master
del master.zip


rem update dayjs -----------------
curl -L -O https://cdn.jsdelivr.net/npm/dayjs/dayjs.min.js
del .\app\src\main\assets\www\dayjs\dayjs.min.js
move dayjs.min.js .\app\src\main\assets\www\dayjs\


rem update leaflet -----------------
curl -L -O https://cdn.jsdelivr.net/npm/leaflet/dist/leaflet.min.js
del .\app\src\main\assets\www\leaflet\leaflet.min.js
move leaflet.min.js .\app\src\main\assets\www\leaflet\
curl -L -O https://cdn.jsdelivr.net/npm/leaflet/dist/leaflet.min.css
del .\app\src\main\assets\www\leaflet\leaflet.min.css
move leaflet.min.css .\app\src\main\assets\www\leaflet\


rem update jquery -----------------
curl -L -O https://cdn.jsdelivr.net/npm/jquery/dist/jquery.min.js
del .\app\src\main\assets\www\jquery\jquery.min.js
move jquery.min.js .\app\src\main\assets\www\jquery\


rem update bootswatch -----------------
curl -L -O https://cdn.jsdelivr.net/npm/bootswatch/dist/spacelab/bootstrap.min.css
del .\app\src\main\assets\www\bootstrap\bootstrap.min.css
move bootstrap.min.css .\app\src\main\assets\www\bootstrap\


rem update bootstrap -----------------
curl -L -O https://cdn.jsdelivr.net/npm/bootstrap/dist/js/bootstrap.bundle.min.js
del .\app\src\main\assets\www\bootstrap\bootstrap.bundle.min.js
move bootstrap.bundle.min.js .\app\src\main\assets\www\bootstrap\


rem update flight database -----------------
del .\app\src\main\assets\www\database\navaid.js
del .\app\src\main\assets\www\database\procedure.js
echo var navaids = > .\app\src\main\assets\www\database\navaid.js
echo var procedures = > .\app\src\main\assets\www\database\procedure.js
curl https://raw.githubusercontent.com/lancard/korea-flight-database/master/database/navaid.json >> .\app\src\main\assets\www\database\navaid.js
curl https://raw.githubusercontent.com/lancard/korea-flight-database/master/database/procedure.json >> .\app\src\main\assets\www\database\procedure.js