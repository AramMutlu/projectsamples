# NodeJS

## P2P.js
Dit is een NodeJS server die verbinding maakt met de P2P module. We maken hier gebruik van Express om te communiceren tussen de NodeJS en de webpagina.

Om de module te detecteren loopen we langs alle COM poorten totdat we er een hebben gevonden waarmee je kan verbinden. Hierna wordt de SerialPort module geinitialiseerd en wordt er geluisterd naar berichten.

Wanneer de gebruiker op de webpagina localhost:3000 een bericht wil verzenden krijgen we dat binnen op "/post". Hier wordt het bericht uiteindelijk in de NodeJS via de SerialPort module verstuurd.

Wanneer we een bericht ontvangen wordt het toegevoegd aan een buffer. Als we het complete bericht hebben wordt het weggeschreven naar receivedMessage, waarna een GET request op de webpagina dat bericht ophaalt.

## UDP.js
Dit is wederom een NodeJS server maar deze zet een Server en Client socket op. Wanneer deze opgezet zijn wordt er geluisterd naar messages, en wordt dat op dezelfde manier afgehandeld als hierboven. Wanneer de gebruiker op de webpagina kiest voor verzenden wordt het bericht gewoon via UDP verzonden, zie de code voor meer toelichting

## Globale protocol
Helaas is het niet gelukt om voor de deadline het globale protocol volledig te implementeren, we zijn wel aardig opweg gekomen met het globale protocol. Omdat het niet lukte om voor de deadline dit protocol volledig te implementeren hebben we ervoor gekozen het globale protocol helemaal niet meer te gebruiken in de UDP / P2P clients, hierdoor staat er wat code uitgecomment in deze clients.
De bestanden voor het globale protocol staan in het submapje `global`.
