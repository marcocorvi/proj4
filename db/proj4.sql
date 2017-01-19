PRAGMA foreign_keys=OFF;
BEGIN TRANSACTION;
DROP TABLE config;
DROP TABLE continent;
DROP TABLE country;
DROP TABLE crs;
DROP TABLE relation;
COMMIT;
BEGIN TRANSACTION;
CREATE TABLE config (key TEXT, value TEXT );
INSERT INTO "config" VALUES('version','3');
COMMIT;
PRAGMA foreign_keys=OFF;
BEGIN TRANSACTION;
CREATE TABLE crs( id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, desc TEXT, digits INTEGER DEFAULT 8 );
/* UTM */
INSERT INTO "crs" VALUES(32629,'UTM29N','+proj=utm +zone=29 +ellps=WGS84 +datum=WGS84 +units=m +nodefs',2);
INSERT INTO "crs" VALUES(32630,'UTM30N','+proj=utm +zone=30 +ellps=WGS84 +datum=WGS84 +units=m +nodefs',2);
INSERT INTO "crs" VALUES(32631,'UTM31N','+proj=utm +zone=31 +ellps=WGS84 +datum=WGS84 +units=m +nodefs',2);
INSERT INTO "crs" VALUES(32632,'UTM32N','+proj=utm +zone=32 +ellps=WGS84 +datum=WGS84 +units=m +nodefs',2);
INSERT INTO "crs" VALUES(32633,'UTM33N','+proj=utm +zone=33 +ellps=WGS84 +datum=WGS84 +units=m +nodefs',2);
INSERT INTO "crs" VALUES(32634,'UTM34N','+proj=utm +zone=34 +ellps=WGS84 +datum=WGS84 +units=m +nodefs',2);
INSERT INTO "crs" VALUES(32635,'UTM35N','+proj=utm +zone=35 +ellps=WGS84 +datum=WGS84 +units=m +nodefs',2);
/* ED 1950 */
INSERT INTO "crs" VALUES(23029,'ED50-29','+proj=utm +zone=29 +ellps=intl +units=m +nodefs',2);
INSERT INTO "crs" VALUES(23030,'ED50-30','+proj=utm +zone=30 +ellps=intl +units=m +nodefs',2);
INSERT INTO "crs" VALUES(23031,'ED50-31','+proj=utm +zone=31 +ellps=intl +units=m +nodefs',2);
INSERT INTO "crs" VALUES(23032,'ED50-32','+proj=utm +zone=32 +ellps=intl +units=m +nodefs',2);
INSERT INTO "crs" VALUES(23033,'ED50-33','+proj=utm +zone=33 +ellps=intl +units=m +nodefs',2);
INSERT INTO "crs" VALUES(23034,'ED50-34','+proj=utm +zone=34 +ellps=intl +units=m +nodefs',2);
INSERT INTO "crs" VALUES(23035,'ED50-35','+proj=utm +zone=35 +ellps=intl +units=m +nodefs',2);
/* ITALY */
INSERT INTO "crs" VALUES(26591,'Gauss-Boaga Zone 1','+proj=tmerc +ellps=intl +lat_0=0 +lon_0=9 +k=0.9996 +x_0=1500000 +y_0=0 +towgs84=-104.1, -49.1, -9.9, 0.971, -2.971, 0.714, -11.68 +nodefs',2);
INSERT INTO "crs" VALUES(26592,'Gauss-Boaga Zone 2','+proj=tmerc +ellps=intl +lat_0=0 +lon_0=15 +k=0.9996 +x_0=2520000 +y_0=0 +towgs84=-104.1, -49.1, -9.9, 0.971, -2.971, 0.714, -11.68 +nodefs',2);
INSERT INTO "crs" VALUES(26593,'Gauss-Boaga Sardinia','+proj=tmerc +ellps=intl +lat_0=0 +lon_0=9 +k=0.9996 +x_0=1500000 +y_0=0 +towgs84=-168.6, -34.0, 38.6, -0.374, -0.679, -1.379, -9.48 +nodefs',2);
INSERT INTO "crs" VALUES(26594,'Gauss-Boaga Sicily','+proj=tmerc +ellps=intl +lat_0=0 +lon_0=15 +k=0.9996 +x_0=2520000 +y_0=0 +towgs84=-50.2,-50.4, 84.8, -0.690, -2.012, 0.459, -28.08 +nodefs',2);
/* GERMANY */
INSERT INTO "crs" VALUES(2860, 'Germany - west of 6°E', '+proj=lcc +lat_1=45.5 +lat_2=44.25 +lat_0=43.83333333333334 +lon_0=-90 +x_0=600000 +y_0=0 +ellps=GRS80 +units=m +no_defs', 2 );
INSERT INTO "crs" VALUES(2861, 'Germany 6-12°E', '+proj=lcc +lat_1=44.06666666666667 +lat_2=42.73333333333333 +lat_0=42 +lon_0=-90 +x_0=600000 +y_0=0 +ellps=GRS80 +units=m +no_defs', 2 );
INSERT INTO "crs" VALUES(2862, 'Germany - east of 12°E', '+proj=tmerc +lat_0=40.5 +lon_0=-105.1666666666667 +k=0.9999375 +x_0=200000 +y_0=0 +ellps=GRS80 +units=m +no_defs', 2 );
INSERT INTO "crs" VALUES(2898, 'Germany - Berlin', '+proj=lcc +lat_1=43.66666666666666 +lat_2=42.1 +lat_0=41.5 +lon_0=-84.36666666666666 +x_0=3999999.999984 +y_0=0 +ellps=GRS80 +to_meter=0.3048 +no_defs', 2 );
INSERT INTO "crs" VALUES(2326, 'West Germany', '+proj=tmerc +lat_0=22.31213333333334 +lon_0=114.1785555555556 +k=1 +x_0=836694.05 +y_0=819069.8 +ellps=intl +towgs84=-162.619,-276.959,-161.764,0.067753,-2.24365,-1.15883,-1.09425 +units=m +no_defs', 2 );
INSERT INTO "crs" VALUES(2541, 'West Germany N', '+proj=tmerc +lat_0=0 +lon_0=75 +k=1 +x_0=25500000 +y_0=0 +ellps=krass +units=m +no_defs', 2 );
INSERT INTO "crs" VALUES(2542, 'West Germany C', '+proj=tmerc +lat_0=0 +lon_0=78 +k=1 +x_0=26500000 +y_0=0 +ellps=krass +units=m +no_defs', 2 );
INSERT INTO "crs" VALUES(2543, 'West Germany S', '+proj=tmerc +lat_0=0 +lon_0=81 +k=1 +x_0=27500000 +y_0=0 +ellps=krass +units=m +no_defs', 2 );
/* GREECE */
INSERT INTO "crs" VALUES(3254, 'Greece', '+proj=lcc +lat_1=-72.66666666666667 +lat_2=-75.33333333333333 +lat_0=-90 +lon_0=81 +x_0=0 +y_0=0 +ellps=WGS84 +datum=WGS84 +units=m +no_defs', 2 );
/* SPAIN */
INSERT INTO "crs" VALUES(2062, 'Madrid 1870', '+proj=lcc +lat_1=40 +lat_0=40 +lon_0=0 +k_0=0.9988085293 +x_0=600000 +y_0=600000 +a=6378298.3 +b=6356657.142669561 +pm=madrid +units=m +no_defs', 2 );
INSERT INTO "crs" VALUES(3199, 'Canary Islands', '+proj=utm +zone=32 +ellps=intl +towgs84=-208.406,-109.878,-2.5764,0,0,0,0 +units=m +no_defs', 2 );
INSERT INTO "crs" VALUES(3629, 'Canary - west of 18°W', '+proj=tmerc +lat_0=40 +lon_0=-78.58333333333333 +k=0.9999375 +x_0=350000 +y_0=0 +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs', 2 );
INSERT INTO "crs" VALUES(2335, 'Balearic Islands', '+proj=tmerc +lat_0=0 +lon_0=123 +k=1 +x_0=21500000 +y_0=0 +a=6378140 +b=6356755.288157528 +units=m +no_defs', 2 );
INSERT INTO "crs" VALUES(2336, 'Spain mainland', '+proj=tmerc +lat_0=0 +lon_0=129 +k=1 +x_0=22500000 +y_0=0 +a=6378140 +b=6356755.288157528 +units=m +no_defs', 2 );
INSERT INTO "crs" VALUES(2337, 'Spain North-West', '+proj=tmerc +lat_0=0 +lon_0=135 +k=1 +x_0=23500000 +y_0=0 +a=6378140 +b=6356755.288157528 +units=m +no_defs', 2 );
INSERT INTO "crs" VALUES(2338, 'Portugal and Spain', '+proj=tmerc +lat_0=0 +lon_0=75 +k=1 +x_0=500000 +y_0=0 +a=6378140 +b=6356755.288157528 +units=m +no_defs', 2 );
INSERT INTO "crs" VALUES(2366, 'Spain - onshore', '+proj=tmerc +lat_0=0 +lon_0=126 +k=1 +x_0=42500000 +y_0=0 +a=6378140 +b=6356755.288157528 +units=m +no_defs', 2 );
INSERT INTO "crs" VALUES(2367, 'Spain North-East' ,'+proj=tmerc +lat_0=0 +lon_0=129 +k=1 +x_0=43500000 +y_0=0 +a=6378140 +b=6356755.288157528 +units=m +no_defs', 2 );
INSERT INTO "crs" VALUES(2368, 'Spain - South-West', '+proj=tmerc +lat_0=0 +lon_0=132 +k=1 +x_0=44500000 +y_0=0 +a=6378140 +b=6356755.288157528 +units=m +no_defs', 2 );
/* ROMANIA */
INSERT INTO "crs" VALUES(2546, 'Romania', '+proj=tmerc +lat_0=0 +lon_0=90 +k=1 +x_0=30500000 +y_0=0 +ellps=krass +units=m +no_defs', 2 );
/* FRANCE */
INSERT INTO "crs" VALUES(27562, 'Lambert Centre France', '+proj=lcc +lat_1=46.8 +lat_0=46.8 +lon_0=0 +k_0=0.99987742 +x_0=600000 +y_0=200000 +a=6378249.2 +b=6356515 +towgs84=-168,-60,320,0,0,0,0 +pm=paris +units=m +no_defs', 2);
INSERT INTO "crs" VALUES(27651, 'Lambert Nord France', '+proj=lcc +lat_1=49.50000000000001 +lat_0=49.50000000000001 +lon_0=0 +k_0=0.999877341 +x_0=600000 +y_0=200000 +a=6378249.2 +b=6356515 +towgs84=-168,-60,320,0,0,0,0 +pm=paris +units=m +no_defs', 2 );
INSERT INTO "crs" VALUES(27563, 'Lambert Sud France', '+proj=lcc +lat_1=44.10000000000001 +lat_0=44.10000000000001 +lon_0=0 +k_0=0.999877499 +x_0=600000 +y_0=200000 +a=6378249.2 +b=6356515 +towgs84=-168,-60,320,0,0,0,0 +pm=paris +units=m +no_defs', 2 );
INSERT INTO "crs" VALUES(27571, 'Lambert zone I', '+proj=lcc +lat_1=49.50000000000001 +lat_0=49.50000000000001 +lon_0=0 +k_0=0.999877341 +x_0=600000 +y_0=1200000 +a=6378249.2 +b=6356515 +towgs84=-168,-60,320,0,0,0,0 +pm=paris +units=m +no_defs', 2 );
INSERT INTO "crs" VALUES(27572, 'Lambert zone II', '+proj=lcc +lat_1=46.8 +lat_0=46.8 +lon_0=0 +k_0=0.99987742 +x_0=600000 +y_0=2200000 +a=6378249.2 +b=6356515 +towgs84=-168,-60,320,0,0,0,0 +pm=paris +units=m +no_defs', 2 );
INSERT INTO "crs" VALUES(27573, 'Lambert zone III', '+proj=lcc +lat_1=44.10000000000001 +lat_0=44.10000000000001 +lon_0=0 +k_0=0.999877499 +x_0=600000 +y_0=3200000 +a=6378249.2 +b=6356515 +towgs84=-168,-60,320,0,0,0,0 +pm=paris +units=m +no_defs', 2 );
INSERT INTO "crs" VALUES(27574, 'Lambert zone IV', '+proj=lcc +lat_1=42.16500000000001 +lat_0=42.16500000000001 +lon_0=0 +k_0=0.99994471 +x_0=234.358 +y_0=4185861.369 +a=6378249.2 +b=6356515 +towgs84=-168,-60,320,0,0,0,0 +pm=paris +units=m +no_defs', 2 );
/* SWITZERLAND */
INSERT INTO "crs" VALUES(21781,'CH1903+ / LV95','+proj=somerc +lat_0=46.95240555555556 +lon_0=7.439583333333333 +k_0=1 +x_0=2600000 +y_0=1200000 +ellps=bessel +towgs84=674.374,15.056,405.346,0,0,0,0 +units=m +no_defs',0);
INSERT INTO "crs" VALUES(21782,'CH1903 / LV03','+proj=somerc +lat_0=46.95240555555556 +lon_0=7.439583333333333 +k_0=1 +x_0=600000 +y_0=200000 +ellps=bessel +towgs84=674.374,15.056,405.346,0,0,0,0 +units=m +no_defs',0);
/* POLAND */
INSERT INTO "crs" VALUES(3120, 'Poland zone I', '+proj=sterea +lat_0=50.625 +lon_0=21.08333333333333 +k=0.9998 +x_0=4637000 +y_0=5467000 +ellps=krass +towgs84=33.4,-146.6,-76.3,-0.359,-0.053,0.844,-0.84 +units=m +no_defs', 2 );
INSERT INTO "crs" VALUES(2172, 'Poland zone II', '+proj=sterea +lat_0=53.00194444444445 +lon_0=21.50277777777778 +k=0.9998 +x_0=4603000 +y_0=5806000 +ellps=krass +towgs84=33.4,-146.6,-76.3,-0.359,-0.053,0.844,-0.84 +units=m +no_defs', 2 );
INSERT INTO "crs" VALUES(2173, 'Poland zone III', '+proj=sterea +lat_0=53.58333333333334 +lon_0=17.00833333333333 +k=0.9998 +x_0=3501000 +y_0=5999000 +ellps=krass +towgs84=33.4,-146.6,-76.3,-0.359,-0.053,0.844,-0.84 +units=m +no_defs', 2 );
INSERT INTO "crs" VALUES(2174, 'Poland zone IV', '+proj=sterea +lat_0=51.67083333333333 +lon_0=16.67222222222222 +k=0.9998 +x_0=3703000 +y_0=5627000 +ellps=krass +towgs84=33.4,-146.6,-76.3,-0.359,-0.053,0.844,-0.84 +units=m +no_defs', 2 );
INSERT INTO "crs" VALUES(2175, 'Poland zone V', '+proj=tmerc +lat_0=0 +lon_0=18.95833333333333 +k=0.999983 +x_0=237000 +y_0=-4700000 +ellps=krass +towgs84=33.4,-146.6,-76.3,-0.359,-0.053,0.844,-0.84 +units=m +no_defs', 2 );
/* IRAN */
INSERT INTO "crs" VALUES(2110, 'Tuhirangi 2000', '+proj=tmerc +lat_0=-39.51222222222222 +lon_0=175.64 +k=1 +x_0=400000 +y_0=800000 +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs', 2 );
INSERT INTO "crs" VALUES(27210, 'Tuhirangi Circuit', '+proj=tmerc +lat_0=-39.51247038888889 +lon_0=175.6400368055556 +k=1 +x_0=300000 +y_0=700000 +ellps=intl +datum=nzgd49 +units=m +no_defs', 2 );
INSERT INTO "crs" VALUES(2057, 'Rassadiran / Nakhl e Taqi', '+proj=omerc +lat_0=27.51882880555555 +lonc=52.60353916666667 +alpha=0.5716611944444444 +k=0.999895934 +x_0=658377.437 +y_0=3044969.194 +ellps=intl +towgs84=-133.63,-157.5,-158.62,0,0,0,0 +units=m +no_defs', 2 );
/*
INSERT INTO "crs" VALUES( );
INSERT INTO "relation" VALUES( );
*/
COMMIT;
PRAGMA foreign_keys=OFF;
BEGIN TRANSACTION;
CREATE TABLE continent ( id INTEGER PRIMARY KEY, name TEXT );
INSERT INTO "continent" VALUES(1,'Europe');
INSERT INTO "continent" VALUES(2,'Asia');
INSERT INTO "continent" VALUES(3,'Africa');
INSERT INTO "continent" VALUES(4,'North America');
INSERT INTO "continent" VALUES(5,'Central America');
INSERT INTO "continent" VALUES(6,'South America');
INSERT INTO "continent" VALUES(7,'Oceania');
COMMIT;
PRAGMA foreign_keys=OFF;
BEGIN TRANSACTION;
CREATE TABLE country( code TEXT, name TEXT , continent INTEGER);
INSERT INTO "country" VALUES('fr','France',1);
INSERT INTO "country" VALUES('gr','Greece',1);
INSERT INTO "country" VALUES('de','Germany',1);
INSERT INTO "country" VALUES('it','Italy',1);
INSERT INTO "country" VALUES('po','Poland',1);
INSERT INTO "country" VALUES('pt','Portugal',1);
INSERT INTO "country" VALUES('ro','Romania',1);
INSERT INTO "country" VALUES('es','Spain',1);
INSERT INTO "country" VALUES('ch','Switzerland',1);
INSERT INTO "country" VALUES('uk','United Kingdom',1);
INSERT INTO "country" VALUES('ir','Iran',2);
INSERT INTO "country" VALUES('au','Australia',7);
COMMIT;
PRAGMA foreign_keys=OFF;
BEGIN TRANSACTION;
CREATE TABLE relation( country TEXT, crs INTEGER );
INSERT INTO "relation" VALUES('it',26591);
INSERT INTO "relation" VALUES('it',26592);
INSERT INTO "relation" VALUES('it',26593);
INSERT INTO "relation" VALUES('it',26594);
INSERT INTO "relation" VALUES('it',23032);
INSERT INTO "relation" VALUES('it',23033);
INSERT INTO "relation" VALUES('it',32632);
INSERT INTO "relation" VALUES('it',32633);
INSERT INTO "relation" VALUES('po',3120); /* POLAND */
INSERT INTO "relation" VALUES('po',2172);
INSERT INTO "relation" VALUES('po',2173);
INSERT INTO "relation" VALUES('po',2174);
INSERT INTO "relation" VALUES('po',2175);
INSERT INTO "relation" VALUES('po',32634);
INSERT INTO "relation" VALUES('de',2541); /* GERMANY */
INSERT INTO "relation" VALUES('de',2542);
INSERT INTO "relation" VALUES('de',2543);
INSERT INTO "relation" VALUES('de',2326);
INSERT INTO "relation" VALUES('de',2898);
INSERT INTO "relation" VALUES('de',2860);
INSERT INTO "relation" VALUES('de',2861);
INSERT INTO "relation" VALUES('de',2862);
INSERT INTO "relation" VALUES('de',32632);
INSERT INTO "relation" VALUES('de',32633);
INSERT INTO "relation" VALUES('fr',27561); /* FRANCE */
INSERT INTO "relation" VALUES('fr',27562);
INSERT INTO "relation" VALUES('fr',27563);
INSERT INTO "relation" VALUES('fr',27571);
INSERT INTO "relation" VALUES('fr',27572);
INSERT INTO "relation" VALUES('fr',27573);
INSERT INTO "relation" VALUES('fr',27574);
INSERT INTO "relation" VALUES('fr',32630);
INSERT INTO "relation" VALUES('fr',32631);
INSERT INTO "relation" VALUES('fr',32632);
INSERT INTO "relation" VALUES('ch',32632); /* SWITZERLAND */
INSERT INTO "relation" VALUES('ch',21781);
INSERT INTO "relation" VALUES('ch',21782);
INSERT INTO "relation" VALUES('gr',3254); /* GREECE */
INSERT INTO "relation" VALUES('gr',32634);
INSERT INTO "relation" VALUES('gr',32635);
INSERT INTO "relation" VALUES('es',2062); /* SPAIN */
INSERT INTO "relation" VALUES('es',2335);
INSERT INTO "relation" VALUES('es',2336);
INSERT INTO "relation" VALUES('es',2337);
INSERT INTO "relation" VALUES('es',2338);
INSERT INTO "relation" VALUES('es',2366);
INSERT INTO "relation" VALUES('es',2367);
INSERT INTO "relation" VALUES('es',2368);
INSERT INTO "relation" VALUES('es',3629);
INSERT INTO "relation" VALUES('es',32629);
INSERT INTO "relation" VALUES('es',32630);
INSERT INTO "relation" VALUES('es',32631);
INSERT INTO "relation" VALUES('pt',32629); /* PORTUGAL */
INSERT INTO "relation" VALUES('ro',2546);  /* ROMANIA */
INSERT INTO "relation" VALUES('uk',32630); /* UNITED KINGDOM */
INSERT INTO "relation" VALUES('uk',32631);
INSERT INTO "relation" VALUES('ir',2110); /* IRAN */
INSERT INTO "relation" VALUES('ir',27210);
INSERT INTO "relation" VALUES('ir',2057);
COMMIT;
