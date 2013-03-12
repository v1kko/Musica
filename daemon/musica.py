from bottle import route, run
import musicapi


@route('/play')
def play():
    instance.play()


@route('/stop')
def stop():
    instance.stop()

if __name__ == '__main__':
    instance = musicapi.instance('iTunes')
    run(port=9042)
