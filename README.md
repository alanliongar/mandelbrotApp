<h1>Mandelbrot & Buddhabrot Generator</h1>
MandelbrotApp is a Kotlin Android application that visualizes fractals, including the classic Mandelbrot Set and the fascinating Buddhabrot. <br>
The app renders fractals using mathematical precision and allows different color schemes and sampling resolutions for high-quality images.

<h1>🧠 Features</h1>
Mandelbrot Set: Black-and-white or colored rendering using iteration-based weighting.<br>
Buddhabrot Visualization: High-resolution images with enhanced statistical sampling.<br>
Color Modes: Supports log, simple, and gamma weighting to control brightness distribution.<br>
Custom Sampling Factor: Fine-grained control over the number of complex points per pixel.<br>
Optimized Core: Heavy computation offloaded to background thread via Dispatchers.Default.<br>

<h1>🖼️ Screenshots </h1>
<p float="left">
  <img src="https://github.com/alanliongar/mandelbrotApp/blob/master/screenshots/Screenshot_01.png" width="250" />
  <img src="https://github.com/alanliongar/mandelbrotApp/blob/master/screenshots/Screenshot_02.png" width="250" />
  <img src="https://github.com/alanliongar/mandelbrotApp/blob/master/screenshots/Screenshot_03.png" width="250" />
</p>

<h1>🧰 Technologies </h1>
Kotlin (100%)<br>
Android Jetpack Compose<br>
Bitmap.createBitmap for image generation<br>
Coroutine-based background processing (lifecycleScope, Dispatchers)<br>
Mathematical engine built from scratch (no external libraries)<br>

<h1>📍 Fractal Parameters</h1>
Complex plane range: [-1.5, -1.0] to [0.8, 1.0]<br>
Resolution: adjustable (width x height)<br>
Iterations: configurable (e.g., 20,000 for high quality)<br>
Buddha sampling factor: configurable (e.g., 5x5 per pixel)<br>

<h1>🧪 How to Run via Command Line (Windows)</h1>
Open the powershell terminal.<br>
Compile the file .kt (which contains the main function):<br>
<b>kotlinc Main.kt -include-runtime -d MandelbrotApp.jar</b>

and then run:<br>
<b>java -jar MandelbrotApp.jar</b>

🖼️ This will save the .png images directly to the configured folder (e.g., C:\Users\Alan\Desktop\), with filenames formatted by type of calculation, color, and method.<br>

<h1>📂 Output</h1>
Mandelbrot (black-and-white)<br>
Mandelbrot (logarithmic, gamma, simple)<br>
Buddhabrot (logarithmic, gamma, simple)<br>
Each image exported with the naming pattern:<br>
Mandelbrot_{mode}_{foregroundColor}_{backgroundColor}.png<br>
Buddha_{mode}_{foregroundColor}_{backgroundColor}.png<br>

And you can also run on the mobile which is the main application.

## License
```
The MIT License (MIT)

Copyright (c) 2024 Alan Lucindo Gomes

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
```

