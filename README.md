
This project was created as something for me to do in my free time, 
optimizations will be infrequent, but are planned.

Currently the program is single threaded and takes ~20 seconds to process the included image on a 6700HQ.
For the sake of your sanity, I recommend using smaller images.

For larger images, larger kernels are required to obtain the desired results.
The current image uses the 7x7 kernel, more kernels are in the works.

# Planned Features
* Multithreaded convolution (Somewhat implemented, not functional)
* OpenCL support (or possibly CUDA, whatever ends up happening)
* Dynamically generated kernels for different levels of detail

If you find this project interesting, feel free to use any of the code provided.

Feel free to contact me for any questions _bradly@orbisnetwork.net_
