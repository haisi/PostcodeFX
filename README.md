# CUIE Abschlussprojekt


## Bearbeitet von
 - Vorname, Nachname
 
## Integriert in die OOP2-Projekte von
- _**Ergänzen Sie hier welche OOP2-Teams Ihr Custom Control erfolgreich integriert haben.**_


## Abgabe
- **Mittwoch, 5.6.2019**, 20:42 Uhr

- Die Abgabe erfolgt durch ein "Push" auf den Master-Branch Ihres GitHub-Repositories.


## Initiale Schritte
 - Tragen Sie ihren Namen unter "Bearbeitet von" ein.
 
 - Benennen Sie das Package "project" um. Verwenden Sie den GitHub-Account-Namen. (Via "Refactor -> Rename...")
 
 - Pushen Sie diese Änderungen sofort ins Git-Repository (z.B. via "VCS -> Commit... -> Commit&Push")
 
## Abschliessende Schritte
 - Tragen Sie die Namen der OOP2-Teams, die ihr Custom Control erfolgreich integriert haben, unter "Integriert in die OOP2-Projekte von" ein
 
 
## Aufgabe: Custom Control für ChargingStationsFX 

Entwerfen und implementieren Sie ein Custom Control für das OOP2-Projekt 'ChargingStationsFX' auf Basis JavaFX.
 - Das Custom Control soll eines (oder mehrere) der im OOP2-Projekt verwendeten Standard-Controls 
 (insbesondere `TextField`, `Label`, `Button`, `TableView`) ersetzen.
 - Das Custom Control soll den Benutzer beim jeweiligen Arbeitsschritt optimal unterstützen, beispielsweise durch eine hochspezialisierte Anzeige oder eine 
effiziente Eingabemöglichkeit für einen/mehrere der Werte.
 - Falls ihr Custom Control ein _'einfaches, interaktives Anzeigeelement'_ ist, verwenden Sie bei der Implementierung die Struktur 
 wie im package `template_simplecontrol`.
 - Falls ihr Custom Control ein _'Business Control'_ ist, das z.B. eines der `TextFields` ersetzen soll, 
 verwenden Sie bei der Implementierung die Struktur wie im package `template_businesscontrol`. Schauen Sie sich dieses 
 Template auf jeden Fall an. Es ist bereits recht viel Basis-Funktionalität enthalten.


## Präsentation
- **Montag, 3.6.2019**, 18:00 Uhr, Raum 6.0D13

- Die Custom Controls werden der OOP2-Klasse im Rahmen einer **Poster-Session** präsentiert.
- Die Postersession startet mit einer Kurzpräsentation (1 bis 2 Minuten) aller Custom-Controls.
- Danach werden die Custom-Controls gleichzeitig ausgestellt und Gelegenheit gegeben auf Detailfragen einzugehen.

- Erarbeiten Sie eine geeignete Präsentationsform, so dass das Interesse möglichst vieler OOP2-Teams 
an Ihrem Custom Control geweckt und ihr CustomControl in die jeweiligen OOP2-Projekte integrieren wird. 
Es stehen Stellwände zur Verfügung.

- Organisieren Sie einen Integrationstermin zusammen mit dem OOP2-Team. Dies ist eine Gemeinschafts-Aufgabe von CUIE- und OOP2-Team.
Am Dienstag und Mittwoch Nachmittag stehen die Unterrichtsräume für cuie und oop2 zur Verfügung.


## Bewertung
- Mit einer gut wahrnehmbaren Präsentation Ihres implementierten und lauffähigen Custom Control an der Postersession haben Sie mindestens 
  eine 4.0 erreicht.
  
- **Nicht genügend** ist es wenn Sie an der Postersession nicht oder überwiegend passiv teilnehmen. 
**Das Spielen von Videospielen** wird zum Beispiel als passive Teilnahme angesehen.

- Durch eine gute Code-Qualität des Custom Control, d.h. insbesondere eine klare Struktur des Codes entsprechend der
 im Unterricht erarbeiteten Konzepte, können Sie eine 5.0 erreichen. Mit nicht compilierbarem Code kann keine bessere Note erreicht werden.
 
- Die Qualität des Controls aus Benutzersicht wird daran gemessen wie oft das Control in ein OOP2-Projekt integriert 
 wird. Jede Integration wird mit +0.25 bewertet, jedoch maximal mit +1.5.
 

## Bitte beachten Sie
 - Es wird empfohlen das Projekt in 2-er Teams zu bearbeiten. 
   - Es wird erwartet, dass die Lösung gemeinsam erarbeitet und implementiert wird (Stichwort 'Pair-Programming').
 
 - Falls Sie das Assignment zu zweit bearbeiten:
   - tragen Sie beide Namen unter "Bearbeitet von" ein
   - arbeiten Sie ausschliesslich in einem Repository
   - falls sie beide Zugang zu diesem Repository wollen: tragen Sie die zweite Person als "Collaborator" ein (auf GitHub unter "Settings - > Collaborators & teams")
   - löschen Sie das nicht benötigte Repository (auf GitHub unter "Settings")
   - arbeiten Sie gemeinsam und gleichzeitig an den Aufgaben (Stichwort: Pair-Programming)
   - das Aufteilen und separate Bearbeiten von Aufgaben ist nicht erwünscht
 
 - Ausdrücklich erlaubt und erwünscht ist, dass Sie sich gegebenenfalls Hilfe holen.
 Das Programmierzentrum ist geöffnet und Nachfragen werden zum Beispiel über den im Repository integrierten 
 Issue Tracker oder per Mail gerne beantwortet. 


## Bei Problemen mit dem IntelliJ-Setup
 Es kommt immer wieder mal vor, dass der Setup des IntelliJ-Projekts nicht auf Anhieb funktioniert oder "plötzlich"
 nicht mehr funktioniert.
 
 Sie brauchen in so einem Fall NICHT nochmal den Invitation-Link annehmen oder das Projekt via “Check out from Version Control” oder "git clone ..." nochmal anlegen.
 
 Statt dessen ist es am besten den IntelliJ-Setup neu generieren zu lassen. Dazu verwendet man den File "build.gradle", der eine 
 komplette und IDE-unabhängige Projektbeschreibung enthält.
 
 Die einzelnen Schritte:
 
 - Schliessen Sie alle geöffneten Projekte (File -> Close Project)
 
 - Wählen Sie “OPEN” 
 
 - Es erscheint ein Finder-Fenster mit dem Sie zu ihrem Projekt navigieren.
 
 - Dort wählen Sie den File “build.gradle” aus.
 
 - Beim nächsten Dialog “Open as Project” wählen.
 
 - Beim nächsten Dialog kontrollieren ob der Liberica JDK 11 ausgewählt ist.
 
 - Dann “File already exists” mit YES bestätigen.
 
 - ACHTUNG: Jetzt “Delete existing Project and Import” anklicken.
 
 - Warten, warten, warten.
 
 Wenn alles gut gegangen ist sollte im Project-View der Java-Ordner unter src/main blau sein und der Java-Ordner unter src/test grün.
