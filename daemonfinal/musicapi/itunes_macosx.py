"""MusicAPI by Jurriaan Bremer.

Copyright (C) 2013 Jurriaan Bremer

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

"""
import appscript
import musicapi


class Instance(musicapi.Instance):
    def __init__(self):
        """Initialize AppleScript API to interact with iTunes."""
        self.app = appscript.app('itunes')

    def play(self):
        self.app.play()

    def stop(self):
        self.app.stop()
