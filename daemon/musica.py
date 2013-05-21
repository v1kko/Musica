from bottle import abort, request, route, run
import musicapi
import json
import sys


def authed_route(path, method):
    """Authenticated route."""
    def decorator(fn):
        rt = route(path, method=method)

        def wrapper(*args, **kwargs):
            if request.params.get('pass') != password:
                abort(403, 'You are not authorized to do this.')
            return fn(*args, **kwargs)

        return rt(wrapper)
    return decorator


@authed_route('/play', 'GET')
@authed_route('/play', 'POST')
def play():
    instance.play()


@authed_route('/pause', 'GET')
@authed_route('/pause', 'POST')
def pause():
    instance.pause()


@authed_route('/resume', 'GET')
@authed_route('/resume', 'POST')
def resume():
    instance.play()


@authed_route('/currentsong', 'GET')
@authed_route('/currentsong', 'POST')
def currentsong():
    x = instance.current_song
    currentsong = x.artist, x.album, x.name, x.duration
    return json.dumps(currentsong)


@authed_route('/currentsongs', 'GET')
@authed_route('/currentsongs', 'POST')
def getcurrentsongs():
    return json.dumps(currentsongs)

@authed_route('/stop', 'GET')
@authed_route('/stop', 'POST')
def stop():
    instance.stop()


@authed_route('/next', 'GET')
@authed_route('/next', 'POST')
def next_song():
    instance.next()


@authed_route('/prev', 'GET')
@authed_route('/prev', 'POST')
def prev_song():
    instance.prev()


@authed_route('/volume', 'GET')
@authed_route('/volume', 'POST')
def get_volume():
    return str(instance.volume())


@authed_route('/volume/<volume:int>', 'GET')
@authed_route('/volume/<volume:int>', 'POST')
def set_volume(volume):
    return instance.volume(volume)


if __name__ == '__main__':
    if len(sys.argv) != 2:
        print 'Usage: python %s <password>' % sys.argv[0]
        exit(0)

    print '[+] Starting Musica HTTP Daemon'
    print '[x] Please wait while we load the playlist'

    instance = musicapi.instance('iTunes')
    password = sys.argv[1]

    playlist = instance.current_playlist

    print '[x] Please wait while we process all tracks in the playlist'

    currentsongs = [(x.artist, x.album, x.name, x.duration)
                    for x in playlist.tracks]

    print '[x] Good to go!'

    run(port=9042)
