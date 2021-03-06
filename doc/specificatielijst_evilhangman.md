# Specification.

Your challenge for this project is to implement Evil Hangman as a native iOS app. The overall design and aesthetics of this app are ultimately up to you, but we require that your app meet some requirements. All other details are left to your own creativity and interpretation.

# Features.

> Immediately upon launch, gameplay must start (unless the app was simply backgrounded, in which case gameplay, if in progress prior to backgrounding, should resume).

Niet aan voldaan, echter hebben wij wel een extra menu met een aantal instellingen en een highscoremenu waar naartoe kan worden genavigeerd vanuit het hoofdmenu en kan er met 1 druk op de play-knop het spel gestart worden

> Your app�s front side must display placeholders (e.g., hyphens) for yet-unguessed letters that make clear the word�s length.

Aan voldaan, ongeraden letters worden weergegeven door puntjes en het lettertype van het (gedeeltelijk) geraden woord in beeld is afhankelijk van de lengte van het woord

> Your app�s front side must inform the user (either numerically or graphically) how many incorrect guesses he or she can still make before losing.

Aan voldaan, op het speelscherm is een beeldelement tries left met daarachter het aantal kansen wat de speler nog heeft

> Your app�s front side must somehow indicate to the user which letters he or she has (or, if you prefer, hasn�t) guessed yet.

Aan voldaan, letters die al geraden zijn kunnen niet meer worden gekozen, wanneer deze wel worden gekozen word de user geinformeerd dat ze de letter al gekozen hebben en worden er geen tries afgetrokken.

> The user must be able to input guesses via an on-screen keyboard.

Aan voldaan, op het speelscherm wordt het standaard iOS keyboard getoond

> Your app must only accept as valid input single alphabetical characters (case-insensitively).

Aan voldaan, enkel alfabetische karakters worden geaccepteerd, wanneer de shift-toets is ingedrukt en er dus een hoofdletter word ingevuld zal het systeem "invalid character" melden, maar hier wordt geen try afgetrokken.

> Invalid input (e.g., multiple characters, no characters, characters already inputted, punctuation, etc.) should be ignored (silently or with some sort of alert) but not penalized.

Aan voldaan, er word een subtiele melding gegeven in het speelscherm wanneer een foutief karakter word ingstuurd. Hiervoor word de user niet verder gestraft.

> Your app�s front side must have a title (e.g., Hangman) or logo as well as two buttons: one that flips the UI around to the app�s flipside, the other of which starts a new game.

Gedeeltelijk aan voldaan, in het hoofdmenu staat de titel Evil Hangman en in het speelscherm kan snel een nieuw spel worden gestart door de terugknop gevolgd door de playknop in te drukken. Het optiemenu kan ook worden gekozen vanuit dit hoofdmenu maar niet vanuit het speelmenu direct.

> If the user guesses every letter in some word before running out of chances, he or she should be somehow congratulated, and gameplay should end (i.e., the game should ignore any subsequent keyboard input). If the user fails to guess every letter in some word before running out of chances, he or she should be somehow consoled, and gameplay should end. The front side�s two buttons should continue to operate.

Aan voldaan, de speler word gefeliciteerd en geconsolideerd wanneer hij wint/verliest (en word de huidige ronde beeindigd). Hierna kan met een enkele toetsaanslag het spel vervolgd/herstart worden.

> On your app�s flipside, a user must be able to configure three settings: the length of words to be guessed (the allowed range for which must be [1, n], where n is the length of the longest word in words.plist); the maximum number of incorrect guesses allowed (the allowed range for which must be [1, 26]); and whether or not to be evil. By default, your app must be evil. But if the user opts to disable evil, gameplay should occur in a traditional, non-evil way, whereby the app must choose a word pseudorandomly from the start and stay committed to that word until the game�s end.

Gedeeltelijk aan voldaan, in ons optiemenu kan gekozen worden voor het aantal tries dat de user kan maken voordat hij verloren heeft. Een maximale woordlengte zagen wij vrij weinig meerwaarde in en kon in onze mening vooral voor verwarring zorgen. Wel is er een optie om "Evil Mode" aan of uit te zetten.

> When settings are changed, they should only take effect for new games, not one already in progress, if any.

Aan voldaan, in onze app kan het optiemenu niet worden gekozen zonder een spel af te breken, dus dit probleem is per definitie al verholpen,

> Your app must maintain a history of high scores that�s displayed anytime a game is won or lost. We leave the definition of �high scores� to you, but you should somehow rank the results of at least 10 games (assuming at least 10 games have been won), displaying for each the word guessed and the number of mistakes made (which is presumably low). The history of high scores should persist even when your app is backgrounded or force-quit.

Aan voldaan, wij hebben ervoor gekozen om het aantal juiste guesses achter elkaar zonder een keer een spel te verliezen als highscore te nemen en neit het aantal gevonden letters, omdat vaak voorkomende letters ook makkelijker te raden zijn. Deze highscores worden persistent opgeslagen in NSUserDefaults.

# Implementation Details.

> Your app�s UI should be sized for an iPhone or iPod touch (i.e., 320 � 480 points) with support for, at least, UIInterfaceOrientationPortrait. However, if you or your partner owns an iPad and would prefer to optimize your app for it (i.e., 768 � 1024 points), you may, so long as you inform your TF prior to this project�s deadline.

Aan voldaan, de applicatie werkt met het points systeem van Apple. Omdat de resolutie over beide assen verdubbeld is bij de overgang van de iPhone 3 naar de iPhone 4 is er besloten dat het indelen scherm werkt met points die gewoon 1:1 mapped zijn (met pixels) voor de iPhone 3 en 1:2 mapped voor de iPhone 4. Op de iPhone 5 zal de app waarschijnlijk ook prima te gebruiken zijn maar de extra hoogte in het scherm zal ongebruikt (en dus leeg) blijven

> You must use the contents of words.plist as your universe of possible words. You�re welcome, but not required, to transform it into some other format (e.g., SQLite).

Aan voldaan, de gevraade woordenlijst is gebruikt.

> Your app must come with default values for the flipside�s two settings; those defaults should be set in NSUserDefaults with registerDefaults:. Anytime the user changes those settings, the new values should be stored immediately in NSUserDefaults (so that changes are not lost if the application is terminated).

Gedeeltelijk aan voldaan, wij gebruiken maar een van deze opties, namelijk het aantal pogingen tot de speler af is. Deze slider heeft als standaardwaarde 10 en word opgeslagen in NSUserDefaults bij de eerste keer dat de app gerund word en is dus persistent, elke keer dat de waarde word veranderd word deze ook opgeslagen.

> You must implement each of the flipside�s numeric settings with a UISlider. Each slider should be accompanied by at least one UILabel that reports its current value (as an integer).

Aan voldaan, het aantal tries dat de user heeft voordat hij heeft verloren stelt men in met een slider.

> You must implement the flipside�s evil toggle with a UISwitch.

Aan voldaan, evil mode kan aan en uit worden gezet met een switch.

> You are welcome to implement your UI with Xcode�s interface builder in MainViewController.xib and FlipsideViewController.xib, or you may implement your UI in code in MainViewController.{h,m} and FlipsideViewController.{h,m}.

De interfacebuilder is niet door ons gebruikt voor dit project.
You must obtain a user�s guesses via a UITextField (and the on-screen keyboard that accompanies it). For the sake of aesthetics, you are welcome, but not required, to keep that UITextField hidden (so long as the on-screen keyboard works). You are also welcome, but not required, to respond to user�s keypresses instantly, without waiting for them to hit return or the like, in which case textField:shouldChangeCharactersInRange:replacementString in the UITextFieldDelegate protocol might be of some interest.
Gedeeltelijk aan voldaan, wij nemen inderdaad direct de letters aan via een on-screen keyboard zonder aanslag van de enter toets, echter niet via een UITextField.

> You must implement your app�s two strategies for gameplay (evil and non-evil) in two separate model classes called EvilGameplay and GoodGameplay (in files called EvilGameplay.{h,m} and GoodGameplay.{h,m}, respectively) both of which must implement a protocol called GameplayDelegate (which must be declared in a file called GameplayDelegate.h). In other words, based on whether evil is enabled or disabled, your app should pass messages to an instance of one class or the other.

Gedeeltelijk aan voldaan, wij ondersteunen wel een evil mode en een good mode maar niet op deze specifieke manier

> You must implement the display of high scores in a UIViewController called HistoryViewController (in files called HistoryViewController.{h,m,xib}) that presents itself at game�s end via a UIModalTransitionStyleCoverVertical transition. You must also declare a HistoryViewControllerDelegate protocol (in HistoryViewController.h) that MainViewController implements, much like FlipsideViewController.h declares FlipsideViewControllerDelegate.

Niet aan voldaan, wij ondersteunen wel een prima functionerend highscorescherm maar niet op deze specifieke manier.

> You must implement methods with which to store and retrieve high scores in a model called History(asbycreatingHistory.{h,m}files). Youmuststorehighscorespersistently,as in a property list (other than words.plist) or in some other format (e.g., SQLite).

Gedeeltelijk aan voldaan, wij schrijven de highscores inderdaad weg met een stel methodes, echter anders genoemd. De highscores worden opgeslagen in NSUserDefaults in een tab-separated string

> Your app must use Automatic Reference Counting (ARC).

Niet aan voldaan.

> You must implement unit tests for your models.

(Gedeeltelijk) aan voldaan, we hebben wat unittests gedaan met zoals bijvoorbeeld het laden en saven van gegevens in NSUserDefaults (zoals highscores), maar deze code staat uiteraard niet meer in de uiteindelijke versie.

> Your app must work within the iPhone 5.1 Simulator; you need not test it on actual hardware. However, if you or your partner owns an iPad, iPhone, or iPod touch, and you�d like to install your app on it, see https://manual.cs50.net/iOS for instructions.

Aan voldaan, de app is prima te gebruiken met de iPhone simulator van Xcode.
