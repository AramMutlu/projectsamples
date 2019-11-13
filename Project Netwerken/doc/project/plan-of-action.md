# Plan van aanpak
________________

# Inhoud
1. [Inleiding](#inleiding)
2. [Projectorganisatie](#projectorganisatie)
   1. [Contactgegevens](#contactgegevens)
   2. [Afspraken aanwezigheid](#afspraken-aanwezigheid)
   3. [Communicatie en documentatie](#communicatie-en-documentatie)
   4. [Planning](#planning)
3. [Scrum](#scrum)
   1. [Applicatie](#applicatie)
   2. [Rolverdeling](#rolverdeling)
   3. [Definition of Done](#definition-of-Done)
4. [Deliverables](#deliverables)

________________
# Inleiding
Bij dit project zal onze groep van studenten een intelligent alarmsysteem realiseren. Communicatie in een oorlogssituatie zal als testdoel gebruikt worden.

Om het systeem te bouwen wordt er gebruik gemaakt van 2 lange-afstand antennes en de volgende technieken:

* **LoRaWAN** via Laird LoRa/Bluetooth modules om de communicatie tussen landen te simuleren.

* **LoRa Peer2Peer** via NiceRF Lora611 Pro dongels om de communicatie met verken-eenheden te simuleren.

* **UDP** via WLAN om de communicatie binnen de gevechtsbasis te simuleren.

________________
# Projectorganisatie
## Contactgegevens

| Naam | Telefoonnummer | E-Mail |
| ------------- |:-------------:| -----:|
| Tom Bolks | 06-37664292 | tom.bolks.tb@gmail.com |
| Thijs Brummelhuis | 06-27917815 | 437509@student.saxion.nl |
| Aram Mutlu  | 06-34486312 | aram_mutlu@hotmail.com |
| Kevin Holsink | 06-27124187 | kholsink1994@gmail.com |
| Niels Gerritsjans | 06-20074949| niels.gerritsjans@gmail.com |
| Victor Langemaire | 06-39866852 | victorlangemaire@gmail.com  |
| Toby Nijboer | 06-11976736 | toby.nijboer@gmail.com  |
| Axel Timan | 06-54681029 | axeltiman@gmail.com  |
## Afspraken aanwezigheid
Bij afwezigheid of te laat komen tijdens contacturen dient dit van te voren aangegeven te worden door middel van een mail, telefonisch of via een Whatsapp / Slack bericht. Wanneer dit niet gedaan wordt zal de betreffende persoon een biertje moeten betalen aan alle overige groepsgenoten.
## Communicatie en documentatie
Er wordt gecommuniceerd via Slack en Whatsapp. Het scrumboard wordt bijgehouden via Trello.
## Planning
* (Voorfase (week 1 - 3)
  * Onderzoek
  * Protocol opstellen
  * Ontwerp presenteren 
* Sprint 1 (week 4 - 6)
  * Bouwen prototype
  * Demo
* Sprint 2 (week 7- 8) 
  * Vervolg prototype
  * Demo
* Assessment (week 9 - 10)


# Scrum
## Applicatie
Om stories en andere issues te tracken maken we gebruik van Trello. Op Trello hebben we 1 team met 2 boards (sprint 1 en sprint 2). 
## Rolverdeling
Volgens scrum zijn de volgende rollen te verdelen:
* **Scrum master** (Axel)<br/>
De scrum master is verantwoordelijk voor het hele project en is dus ook het eerste aanspreekpunt. Hij zorgt ervoor dat de documentatie en de resultaten op tijd worden aangeleverd. Ook zal de scrum master er voor zorgen dat het scrum board in orde blijft.
* **Scrum team** (Thijs, Aram, Tom, Victor, Niels, Toby)<br/>
Het scrum team is verantwoordelijk voor het uitvoeren en afmaken van stories. 
* **Documentatiebeheerder** (Kevin)<br/>
De documentatiebeheerder is verantwoordelijk voor alle documenten van dit project en documentatie van de code. Terwijl iedereen verantwoordelijk is voor de inhoud heeft de documentatiebeheerder de taak een uniforme stijl aan te houden. De documentatiebeheerder zorgt ook voor de nodige back-ups.
### Protocol rolverdeling
De volgende mensen zijn verantwoordelijk voor de desbetreffende protocollen en technieken binnen het project:

| Naam | Verantwoordelijkheid |
| --- | --- |
| Kevin | LoRap2p |
| Tom | WLAN |
| Aram | bluetooth |
| Toby | LoRaWAN |
| Axel | Firmware |
| Thijs | Architect |
| Victor | Overkoepelend protocol |
| Niels | NodeJS client |

## Definition of Done
Een user story is klaar als deze positief getest is door een andere teamgenoot. Ook moet hierbij de javadoc af en correct zijn en indien nodig moet de documentatie bijgewerkt zijn.
# Deliverables
Systeem- en projectdossier (doc.zip)
Prototype (src.zip)

Systeemdossier bevat:
* Functioneel ontwerp
* Technisch ontwerp
* Testrapportage

Projectdossier bevat:
* Plan van aanpak
* Testplan/rapport
* Urenverantwoording (team en persoonlijk)
* Procesresultaten (Scrum-zaken)

