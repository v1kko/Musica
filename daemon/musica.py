from bottle import abort, request, route, run
import musicapi
import sys


def authed_route(*args, **kwargs):
    def decorator(fn):
        rt = route(*args, **kwargs)

        def wrapper(*args, **kwargs):
            if request.params.get('pass') != password:
                abort(403, 'You are not authorized to do this.')
            return fn()

        return rt(wrapper)
    return decorator


@authed_route('/play')
def play():
    instance.play()


@authed_route('/stop')
def stop():
    instance.stop()

if __name__ == '__main__':
    if len(sys.argv) != 2:
        print 'Usage: python %s <password>' % sys.argv[0]
        exit(0)

    instance = musicapi.instance('iTunes')
    password = sys.argv[1]

    run(port=9042)
