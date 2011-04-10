/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.badlogic.gdx.backends.openal;

import java.io.ByteArrayOutputStream;

import com.badlogic.gdx.files.FileHandle;

/**
 * @author Nathan Sweet
 */
public class Ogg {
	static public class Music extends OpenALMusic {
		private OggInputStream input;

		public Music (OpenALAudio audio, FileHandle file) {
			super(audio, file);
		}

		protected int read (byte[] buffer) {
			if (input == null) {
				input = new OggInputStream(file.read());
				setup(input.getChannels(), input.getSampleRate());
			}
			return input.read(buffer);
		}

		protected void reset () {
			if (input == null) return;
			input.close();
			input = null;
		}
	}

	static public class Sound extends OpenALSound {
		public Sound (OpenALAudio audio, FileHandle file) {
			super(audio);

			OggInputStream input = new OggInputStream(file.read());
			ByteArrayOutputStream output = new ByteArrayOutputStream(4096);
			byte[] buffer = new byte[2048];
			while (!input.atEnd()) {
				int length = input.read(buffer);
				if (length == -1) break;
				output.write(buffer, 0, length);
			}
			setup(output.toByteArray(), input.getChannels(), input.getSampleRate());
		}
	}
}
