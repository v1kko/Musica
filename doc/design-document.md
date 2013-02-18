# Musica Design Document

## Technical Design

### Objects

![The flowchart](http://5.135.191.67/technicalflowchart.jpg "The Flowchart")

#### Client-side

* Play/Pause Button
    * Used to pause or play the current track
* Rewind Button
    * Used to rewind current song or (when tapped twice in three seconds) to goto
    previous number
* Forward Button
    * Used to skip to next song
* Forward Swipe
    * Used to skip to next song
* Playlist
    * Displays 7 songs with the song playing in the middle and the previous and
    upcoming songs under and above the current song.
* Volume Bar
    * Control the volume, is a button at first but expands at click to a slider
* Progress Bar
    * Tracks the progress of the current song and is draggable to replay or skip
    parts of the song
* Server Communication Object
    * The Main object that can communicate with the server and has an API
    addressable by all other objects to retrieve their information

#### Server-side

* Platform specific plugin
* Server Object

### Features

*  Start and stop your music with a play/pause button                                            
*  Control which track you listen to with a previous and next song button
*  Change the volume with a volume slider
*  Show current track playing
*  Swipe to next track

## Graphical Design

### Sketches

* The main screen

![Alt text](http://5.135.191.67/main.png "The Main Screen")

* The playlist selection screen


* The settings screen

# Musica Style Document

## Technical Style

### Java Style

* Objects in own files
* Commenting of each function
* Code for portability

### Server side Style

* Python and python common style
* Addressable with json and GET request

## Graphical Style

* Vector Based UI's
* Max 3 clicks to function
* Overall same pallet usage (4 color preferably)

# Musica Feature Division

* Victor
    * Make the Play/Pause, Rewind, and Forward swipe/button
    * Make the client-sided communication object and API to the client
* Kas
    * Make the playlist, volume and current progress bar
    * In charge of all the Graphical Designs
* Jurriaan
    * Make the plugins
    * Make the server-sides communication object
    * Design the protocol (API of the server)
