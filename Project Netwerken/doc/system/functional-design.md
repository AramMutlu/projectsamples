# Research
Dit document is een snelle referentie voor leden van deze projectgroep met als inhoud een beknopte samenvatting van te gebruiken technologie en protocollen.
De devices die we gebruiken zijn de _LoRa611 v1.1_, en de _RM186-SM_.

## Inhoud
1. [smartBASIC](#smartBASIC)
2. [Synchronisatie LoRa/BluetoothLE](#Synchronisatie)
4. [NodeJS / UI](#NodeJS)
## smartBASIC <a name="smartBASIC"></a>

smart BASIC is een extensie van de populaire programmeertaal BASIC. Het is ontworpen door Laird om de ontwikkeling van firmware te versimpelen. Het heeft alle structuren van een moderne programmeertaal terwijl het de simpele syntax van BASIC behoudt. De meeste Laird modules komen met een run time engine zodat er meteen ontwikkeld kan worden. 

Een voorbeeld van smartBASIC is:

``` 
print "\nStart Demo\n"
dim i
dim pin_state
pin_state = gpiosetfunc(0,2,0) //Init pin0/D13 Low
while(1) //Loop forever
    WHILE i<400 //Delay
    i = i+1
    ENDWHILE
    gpiowrite(0,1) //Write GPIO High
    i = 0
    WHILE i<400 //Delay
    i = i+1
    ENDWHILE
    gpiowrite(0,0) //Write GPIO LOW
    i = 0
ENDWHILE 
```

In dit project zal er van deze taal gebruik gemaakt worden om de logica van de modules te programmeren. Hoewel de programma's met elke willekeurige text-editor geschreven kunnen worden, wordt Notepad++ aangeraden omdat er een syntax highlighting voor BASIC beschikbaar voor is. De code kan met het programma UwTerminalX gecompiled en gedownload worden op de module.

## Synchronisatie LoRa/BluetoothLE <a name="Synchronisatie"></a>

Er zijn verschillende manieren om synchronisatie te bewerkstelligen binnen LoRa/BLE.

### 1. Advertiser mode

1 node broadcast een synchronisatie packet waar de andere nodes naar luisteren. Deze packets worden ongeveer door alle nodes tegelijk ontvangen. Wanneer een node deze packet ontvangt, slaat hij de lokale tijd op als nulpunt. Deze manier heeft als drawback dat de broadcast node niet gesynchroniseerd wordt met de ontvangende nodes. 

### 2. Connection mode

Als er 2 of meerdere nodes met elkaar gesynchroniseerd moeten worden, is connection mode wellicht handiger. Node 1 stuurt een time stamp (low level) naar node 2. Meteen daarna stuurt node 1 nog een timestamp naar node 2 om verzend delays te achterhalen. Node 2 kan dan de ontvangen timestamps en de timestamp van ontvangst gebruiken om zichzelf met node 1 te synchroniseren.

*Voor dit project lijkt connection mode de betere optie.*

## Seriële communicatie
Bij seriële communicatie worden de bits van de gegevens 1 voor 1 als serie achter elkaar verstuurd over 1 communicatiekanaal. Dit is in tegenstelling met parallelle communicatie waarbij meerdere bits als een geheel worden verstuurd over meerdere parallelle kanalen. 

https://en.wikipedia.org/wiki/Serial_communication#/media/File:Parallel_and_Serial_Transmission.gif

**Waarom gebruiken we seriële communicatie?**
De modules die wij gaan gebruiken werken met seriële verbindingen en moeten daarom onze data op deze manier invoeren in de module.

**Hoe gaan we seriële communicatie toepassen?**
Het encrypte bericht gaan we dus versturen in losse bits. Daarom moeten we dus bytes om gaan zetten naar bits. Door een UART chip te gebruiken hiervoor hoeft de cpu van de pc daar minder aandacht aan te besteden. Zonder chip kan ook maar dan is het wat intensiever voor de cpu.


## LORA611AES

### Power on reset
Zodra de LORA611AES wordt ingeschakeld zal er een rood(TX) en blauw(RX) ledje op het apparaat 3 keer knipperen.


### Working mode
De CS en SET pins staan standaard intern uitgeschakeld. Als je de CS pin op high zet of open laat dan staat de module in working mode. In deze modus wacht het apparaat totdat er een serieel signaal of RF signaal binnenkomt.

Wanneer er een serieel signaal binnen komt dan zal het apparaat de invoer checken op errors. Daarna zal het apparaat de ontvangen data automatisch uitzenden via RF als er geen errors zijn.

De COM poort van het apparaat en het programma moeten met elkaar matchen.
De default setting van het apparaat is: 9600, 8, N,1.
Dataflow
De maximale grote van een packet is 62bytes. Als de data dat verstuurd moet worden groter is dan zal het gesplit moeten worden over meerdere packets.

### Communicatie protocol
Het protocol ziet er zo uit:
Baud rate=9600bps; Data bit=8bits; Stop bit:1; Parity bit: none
    Command format : AA FA + command +[parameters]
    command is 1 byte, parameters 0 or 14 bytes in Hex format.
    Return value ended with “\r\n”.

### Commando’s

**Module naam en versie opvragen**
AA FA AA

response = LORA611AES_VER3.0\r\n

**Settings uitlezen**
AA FA 01

response = RF channel / RF band / RF data rate / RF power / Serial data rate/Series Data bit / Series Stop bit/Series Parity bit / NET ID / NODE ID / Key / Keydata/ \r\n

**Resetten naar default settings**
AA FA 02

response = “OK\r\n” or “ERROR\r\n”

De default settings zijn:
Frequency: Tx = Rx = CH20 = 433.92 MHz    (Band = 433MHz)
RF data rate: Tx= Rx=9600 bps
RF power=7 (Max output）
Serial: baud rate=656 bps    Data bit= 8 Bits Stop bit= 1 Bits Parity bit=None
NET ID = 00 00 00 00        NODE ID = 00 00

**Settings instellen**
De lengte van dit commando is 16 bytes.
formaat: AA FA 03 RF Channel / RF Band / RF Rate / RF Power / Serial transmission date / data bits / stop bits / parity / NET ID / NODE ID/Key /Key value

command in 16 bytes:
AA FA 03 XX XX XX XX XX XX XX XX XX XX XX XX XX XX XX XX XX XX XX XX XX XX XX XX XX XX XX XX XX XX XX

response = “OK\r\n” or “ERROR\r\n”

Beschrijving van de parameters zijn hier te vinden: http://www.nicerf.com/Upload/ueditor/files/2017-01-12/Lora611AES-100mW%20AES%20encrypted%20Anti%20interference%20Lora%20Wireless%20Transceiver%20Data%20Transmission%20Module-d62be0a4-ebd7-493a-82c5-1896e6294b82.pdf

## Android applicatie

De Android applicatie zal worden geschreven in de programmeertaal JAVA. De applicatie zal voldoen aan één tot drie functionaliteiten;

**Berichten versturen en ontvangen via WiFi**

Het communiceren via WiFi zal anders verlopen dan via de Bluetooth protocol. Hiervoor zal mogelijk eerst een hotspot moeten worden opgezet die vervolgens verbinding heeft met de RM186. Door de Socket module in JAVA te gebruiken zal er dankzij de Input en OutputStream berichten kunnen worden verstuurd en ontvangen.

Naast het bovenstaande is het ook mogelijk door gebruik te maken van POST requests te sturen naar de NodeJS server.


**Berichten versturen en ontvangen via Bluetooth**

Doordat de RM186 beschikt over 2.4 Ghz Bluetooth is het mogelijk om deze te verbinden met een mobiele apparaat die beschikt over Bluetooth. Voor het programmeren wordt er gebruikt gemaakt van de BluetoothSocket. De BluetoothSocket zorgt ervoor dat er een verbinding tot stand kan worden gebracht tussen de RM186 en de mobiele apparaat. Door middel van de InputStream en OutputStream is het mogelijk om berichten te versturen en te ontvangen.

**Berichten versturen en ontvangen via seriële poort**

Naast Bluetooth en WiFi is het mogelijk om berichten te versturen en te ontvangen door middel van een seriële poort. Dit houd in dat de mobiele device wordt aangesloten op de RM186 doormiddel van een On the Go (OTG) kabel. Deze optie zal als optioneel worden beschouwd doordat we al een verbinding maken via een NodeJS server.

Het gebruik van de WiFi module zal mogelijk iets complexer in elkaar steken doordat er een extern apparaat wordt gebruikt, bijvoorbeeld via de NodeJS server of een NodeMCU. Wat betreft Bluetooth zal dit makkelijker moeten verlopen, echter moet er tijdens het programmeren van de RM186 rekening gehouden worden met de Bluetooth protocollen.

De applicatie interface zal enkel beschikken over een EditText, Button en een TextView

	**EditText**
	**De gebruiker dient hier een bericht in te typen wat hij wilt versturen.

	**Button**
	**Wanneer de gebruiker het bericht definitief wilt versturen drukt hij op deze knop.

	**TextView**
	**De TextView zal weergeven welke berichten er ontvangen zijn.

Doordat er maar één byte tegelijk kan worden verstuurd wordt de String per karakter verstuurd. Door gebruik te maken van de _.toCharArray()_ methode zal de string gesplitst worden in een array. In een for loop zal elk karakter één voor één worden verstuurd.

Na onderzoek is er een goede uitwerking voor een voorbeeldcode gevonden voor het gebruik van Bluetooth; [https://github.com/talahata/Bluetooth](https://github.com/talahata/Bluetooth) en voor WiFi;

[https://github.com/aidenbarrett/LoRa-Network-Project/tree/master/LoRa-Android-App-Dev/Lora-Network-App](https://github.com/aidenbarrett/LoRa-Network-Project/tree/master/LoRa-Android-App-Dev/Lora-Network-App)


## NodeJS / UI <a name="NodeJS"></a>
### Verbinding
De NodeJS client heeft het LoRa611 device aangesloten via Serial Port. Om te verbinden met het device wordt de npm module `serialport`gebruikt. Met deze module is het mogelijk om naar de verbonden poort data te schrijven. Hiermee kun je dus messages writen naar het LoRa device

### Poort openen
De poort openen met NodeJS gaat als volgt:

```
var SerialPort = require('serialport');
var port = new SerialPort('COM4', {baudRate: 57600});

port.write('message', function(err) {
  if (err) return console.log('Error on write: ', err.message);
  console.log('message written');
});
 
// Open errors will be emitted as an error event
port.on('error', function(err) {
  console.log('Error: ', err.message);
})
```

### Flow
Bij het opstarten van de NodeJS client open je tevens de port die wordt gebruikt door het LoRa device. Hierdoor kun je met POST en GET requests communiceren met de NodeJS, waarna je met de geopende poort communiceert.
 * Bericht ontvangen op NodeJS server<br/>
      Wanneer een bericht is ontvangen moet het worden weergegeven op de webpagina. Dit kan d.m.v. een GET request dat elke seconde checkt.
 * Bericht verzenden vanaf pagina<br/>	
      Bij het verzenden van een bericht kun je een POST request doen. De server handelt dan het daadwerkelijk verzenden van het bericht.

Dit wordt gedaan d.m.v. Express met GET en POST requests. De pagina bevat niets meer dan een invoerveld waar je een message in kan typen, een knop om het bericht te verzenden en een vakje waar je ontvangen berichten ziet.

## Bluetooth Low Energy
Bluetooth low energy (BLE) is een draadloos netwerk die een directe verbinding tussen 2 apparaten ondersteund. Vergeleken met het klassieke Bluetooth zorgt BLE voor hetzelfde werk met veel minder energie (zegt de naam ook al) en kosten. Hierbij behoudt hij ongeveer hetzelfde bereik. 
BLE heeft een kleinere vertraging/latency (6ms) vergeleken met normaal bluetooth (100ms). Ook heeft BLE een minder tijd nodig om data te versturen (3ms) vergeleken met (100ms).

BLE komt ook voor in mobiele telefoons en maakt ook gebruik van hetzelfde 2.4GHz radio frequentie dus kun je in principe 1 antenne delen met een apparaat met het klassieke bluetooth. 

Bluetooth is te implementeren via Android (voor mobiele apparaten, hier is android 4.3 of hoger nodig)  en het Android platform ondersteund dit waardoor je een connectie kan maken andere bluetooth apparaten.

https://www.youtube.com/watch?time_continue=352&v=vUbFB1Qypg8
https://www.lairdtech.com/solutions/embedded-wireless/smartbasic-ble
https://assets.lairdtech.com/home/brandworld/files/Walkthrough%20-%20Applications%20in%20smartBASIC%20(BL600%20and%20BL620).pdf

## Routing (protocol)
Een routing protocol zorgt ervoor dat routers/apparaten (veilig) met elkaar kunnen communiceren. Er wordt een route gekozen om een bericht van node naar node te krijgen dus van A naar B. 

Bepaling beste route:
Routing-protocollen maken gebruik van metrieken (bijv. bandbreedte) om de beste route naar de ontvanger te bepalen. Routing-protocollen houden ook een tabel bij waarin de routing informatie wordt opgeslagen. Nodes praten met elkaar om een die tabel up to date te houden en om steeds te beste route te kiezen.

Als er wordt gekeken naar het plaatje hier rechts zijn er meerdere nodes te zien die met elkaar moeten communiceren. Dus wanneer node A een bericht moet versturen naar node Q is het de bedoeling om via node C en P te gaan en niet een hele omweg te moeten maken. Dit scheelt tijd, moeite en is veiliger (minder kans dat het bericht kwijt raakt).

## Symmetrische encryptie
Bij symmetrische encryptie gebruik je dezelfde key voor het encrypten en het decrypten. 
Deze key is secret voor de buitenwereld omdat deze anders zo gebruikt kan worden om berichten af te kunnen luisteren en/of te veranderen. Beide kanten hebben hierdoor de secret key nodig, zowel de verzender als de ontvanger. Dit is een van de grootste nadelen van deze vorm van encryptie.

Lora gebruikt hier AES128 voor, dit betekent dat de encryptiemethode AES wordt gebruikt, met een key die 128 bits lang is. AES staat voor Advanced Encryption Standard. Dit algoritme werkt via een block cipher, wat betekent dat er een blok van de tekst geëncrypt wordt. De grootte van dit blok is bij AES 128 bits. De lora module kan pakketten van 62 bytes verzenden, alles groter dan dat wordt opgesplitst in pakketjes van 62 bytes. Hierdoor wordt 62 bytes opgesplitst in 128 bits, vervolgens versleuteld en dan gezamenlijk verzonden. De ontvanger vist de pakketjes van 128 bits weer uit het bestand van 62 bytes en decrypt deze waardoor het oorspronkelijke bericht weer terug komt. Deze encryptiemethode is zo veilig dat zelfs de amerikaanse overheid het gebruikt om hun top secret bestanden

De bluetooth module kan pakketten van max 20 bytes verzenden. Dus zij hebben hetzelfde als de lora met 62 bytes, maar dan met 20 bytes. Dankzij bluetooth low energy’s ingebouwde encryptie wordt dit ook symmetrisch geën- en decrypt waardoor dit ook veilig is. 

## Protocollen
Hierin moet alles wat gebeurt, wat het andere team ook zo moet doen precies uitgelegd staan. Dit wordt meestal aangeleverd in een RFC, een request for comments. Als deze wordt goedgekeurd dan wordt dit een STD (standard) en dan wordt dit door iedereen gebruikt. 

In een protocol moet alles wat van de vorige laag van de routing aankomt, wat er mee gedaan wordt en wat er doorgestuurd wordt naar de volgende laag van de routing komen te staan. Hier hebben we 4 protocolspecificaties voor, 1 voor elk van de 3 verschillende modules en 1 voor het globale protocol wat bij elke module geldt. Bij de LoraWan en de lora peer to peer moet goed overlegd worden met de andere groep, omdat deze met elkaar moeten kunnen communiceren. Ook moeten hier afspraken in komen over bijvoorbeeld de frequentie en de encryptie enz.


## LoRaWAN (Bluetooth), LoRa P2P(USB), WLAN(UDP)
We zullen een app maken die gebruik maakt van LoRaWAN via Bluetooth en van WLAN via UDP. We zullen een website maken m.b.v NodeJS die gebruik maakt van LoRa P2P via USB. Voor deze applicatie en website hebben we vooraf protocollen vastgesteld. We hebben daarnaast sockets, frequenties en broadcasts vastgesteld.

LoRa werkt ook heel goed met apparaat netwerken die zich binnenshuis bevinden, ook in moeilijke industriële omgevingen, die anders moeilijke uitdagingen zouden kunnen vormen voor andere technologieën. LoRa is ook zeer schaalbaar en zeer bruikbaar, ondersteunt tot een miljoen nodes en is compatibel met zowel openbare als particuliere netwerken voor de back-van gegevens en tweerichtingscommunicatie. De afweging voor het gebruik van een low-power, ultra-long-range technologie zoals LoRa is doorvoer, waardoor het een slechte fit voor applicaties die gestreamde gegevens nodig hebben. Maar deze beperking speelt geen rol bij onze use case, waar kleine hoeveelheden gebeurtenisgegevens worden geleverd.

**LoRaWan**

LoRaWAN is simpele technologie. Het gaat om een ‘low range’-netwerk, dat op een radiofrequentie van 868 MHz draait. Dat betekent dat het een groter bereik heeft dan andere communicatienetwerken zoals wifi. Omdat de frequentie zo laag is, kost het ook veel minder energie om het netwerk in stand te houden. Een LoRa-sensor kan met batterijen soms wel jaren operationeel blijven. Dat alles maakt het netwerk vooral aantrekkelijk voor kleine, goedkope sensoren waar je niet naar om hoeft te kijken.
Maar er zitten ook nadelen aan het netwerk, met name in de grootte van bestandspakketten die je kunt versturen. Die zijn maar maximaal 55 bytes, en dat maakt LoRaWAN vooral geschikt voor simpele sensoren die slechts kleine boodschappen hoeven te versturen. Het gaat dan ook vooral om binaire gegevens zoals, een sensor die aangeeft wanneer een bepaald waterniveau wordt bereikt of hoe hoog de temperatuur is, of een simpele aan/uit-knop voor lampen.

**LoRa P2P**

Deze communicatie vindt plaats zonder centrale host en alle nodes delen de netwerk resources. De LoRa nodes communiceren onderling zonder gecentraliseerde server.

**WLAN**

Wireless LAN ofwel een draadloos local area netwerk dat vaak ook toegang geeft tot het internet. Hiervoor zijn meerdere apparaten nodig. Via een apparaat koppel je je aan een zogenaamd access point. Deze access point is ook weer een machine dat signalen uitzendt. WLAN is vooral onder WiFi bekend geworden en maakt gebruikt van encryptie methodes zoals WEP(bijna niet meer, minder veilig), WPA en WPA2.

Een UDP bericht bestaat uit een header en een data deel.

De header bestaat uit 4 velden van 2 bytes per veld: 
* bron (poortnummer van het verzendende proces)
* bestemming (poortnummer van het ontvangende proces)
* lengte (aantal bytes in het bericht)
* controle (checksum om fouten te detecteren)

UDP maakt niet eerst een verbinding voordat het de data verstuurd, het verstuurd de data gewoon. Deze data wordt ook niet gecontroleerd of het de correcte bericht is. Bij TCP is dit dan anders hier wordt elk bericht genummerd en de ontvanger stuurt een bericht terug dat het ontvangen is. Bij UDP wordt deze controle allemaal achterwege gelaten zodat berichten sneller en kleiner zijn. Ook moet je bij UDP er vanuit gaan dat je daarom niet alle berichten krijgt.
