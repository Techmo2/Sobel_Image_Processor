# Sobel Image Processor

This project is a very inefficient way to process images with a Sobel filter.

This project was created as something for me to do in my free time, 
optimizations will be infrequent, but are planned.

<s>Currently the program is single threaded and takes ~20 seconds to process the included image on a 6700HQ.</s>
The execution time was reduced. The current provided image takes ~0.7 seconds to complete with the current test, although i'm not sure how quickly the larger image would be processed.

For larger images, larger kernels are required to obtain the desired results.
The current image uses the 7x7 kernel, there is a 3x3 kernel available for smaller images.
Dynamic sizes are in the works.

# Planned Features
* Multithreaded convolution (Somewhat implemented, not functional)
* OpenCL support (or possibly CUDA, whatever ends up happening)
* Dynamically generated kernels for different levels of detail

If you find this project interesting, feel free to use any of the code provided.

Feel free to contact me for any questions _bradly@orbisnetwork.net_
