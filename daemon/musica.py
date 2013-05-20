from bottle import abort, request, route, run
import musicapi
import json
import sys


def authed_route(*args, **kwargs):
    """Authenticated route."""
    def decorator(fn):
        rt = route(*args, **kwargs)

        def wrapper(*args, **kwargs):
            if request.params.get('pass') != password:
                abort(403, 'You are not authorized to do this.')
            return fn()

        return rt(wrapper)
    return decorator


def authed_any_route(*args, **kwargs):
    """Authed Route which supports GET and POST requests."""
    return authed_route(*args, method=['GET', 'POST'], **kwargs)


@authed_any_route('/play')
def play():
    return "OK"
    instance.play()


@authed_any_route('/pause')
def pause():
    return "OK"
    instance.pause()


@authed_any_route('/resume')
def resume():
    instance.play()


@authed_any_route('/getcurrentsongs')
def getcurrentsongs():
    return json.dumps({"currentsongs" : [ "A", "B,", "c", "One -Metallica", "Gangam Style - Psy", "Step One Two - Kaskade", "Bla", "Blalalalalala"]})

@authed_any_route('/getcurrentsong')
def getcurrentsongs():
    return json.dumps({"currentsong" : "One -Metallica"})

@authed_any_route('/stop')
def stop():
    instance.stop()


@authed_any_route('/next')
def next_song():
    instance.next()


@authed_any_route('/prev')
def prev_song():
    instance.prev()


@authed_any_route('/volume')
def get_volume():
    return instance.volume()


@authed_any_route('/volume/<volume>')
def set_volume(volume):
    return instance.volume(volume)


if __name__ == '__main__':
    if len(sys.argv) != 2:
        print 'Usage: python %s <password>' % sys.argv[0]
        exit(0)

    instance = musicapi.instance('iTunes')
    password = sys.argv[1]

    run(port=9042)
