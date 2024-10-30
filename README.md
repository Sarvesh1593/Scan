# Introduction

The **Android Document Scanner App** is a mobile application designed to scan documents using the device's camera. The app utilizes advanced image processing techniques with OpenCV and other libraries to extract and enhance documents from images, providing a clean, high-quality scanned document. Users can easily edit, enhance, crop, and share scanned documents directly from the app.

---

# Features

- **Document Detection**: Automatically detects the document edges and extracts the document from the background.
- **Image Enhancement**: Enhances the scanned document using real-time filters for better readability.
- **Multi-Page Scanning**: Supports scanning multiple pages and compiling them into a single PDF.
- **Image Cropping**: Allows precise cropping of documents to remove unwanted borders.
- **Custom Image Rotation**: Implements a custom feature to rotate scanned images by 90 degrees at a time.
- **Document Sharing**: Share scanned documents via email, cloud storage, or other apps.
- **PDF Export**: Export scanned documents to PDF format for easy sharing and storage.

---

# Architecture

The app follows a modular architecture based on the MVVM (Model-View-ViewModel) pattern. The major components include:

- **UI Layer**: Consists of Activities, Fragments, and Views for the user interface.
- **ViewModel Layer**: Manages UI-related data and handles communication between the UI and the repository.
- **Repository Layer**: Abstracts data sources (local storage, camera data, etc.) and provides a clean API for data access.
- **OpenCV Integration**: Handles the image processing tasks, such as document detection and image enhancement.

---

# Getting Started

### Requirements:

Before you begin, ensure that you have the following tools and knowledge to start developing the app:

- Android Studio installed.
- A physical Android device or emulator.
- Basic knowledge of Kotlin and Android development.
- Familiarity with Android Camera APIs and image processing concepts.

---

# Dependencies

The app leverages several libraries to handle different functionalities:

- **CameraX**: Provides easy-to-use camera functionalities for capturing high-quality images.
- **uCrop**: Allows precise cropping of images with customization options for the user interface.
    - **Implementation**: `implementation("com.github.yalantis:ucrop:2.2.8")`
- **GPUImage**: Applies real-time filters to the scanned document for image enhancement.
    - **Implementation**: `implementation("jp.co.cyberagent.android:gpuimage:2.1.0")`
- **PDFBox**: Assists in creating and manipulating PDFs from scanned documents.
- **Android PDF Viewer**: Allows users to view PDFs within the app.
    - **Implementation**: `implementation("com.github.barteksc:android-pdf-viewer:3.2.0-beta.1")`
- **Glide**: Efficiently loads and displays images in the app.
    - **Implementation**: `implementation("com.github.bumptech.glide:glide:4.11.0")`

---

# Usage

1. **Document Scanning**:
    - Open the app and capture the document using the built-in CameraX API.
    - The app will automatically detect and extract the document from the captured image.
2. **Image Cropping & Rotation**:
    - Use the uCrop library to crop the document and remove unnecessary parts.
    - Rotate the document using the custom rotation feature for proper orientation.
3. **Enhancing the Image**:
    - Apply real-time filters using GPUImage to improve readability, adjust brightness, or convert to grayscale.
4. **PDF Export & Sharing**:
    - Save the scanned document as a PDF and share it via email or cloud storage.

---

# Customization

The app allows customization of the following features:

- **Cropping Tool (uCrop)**: Users can customize the crop tool's UI and cropping ratios.
- **Image Filters (GPUImage)**: Offers various real-time filters to enhance document quality.
- **Custom Rotation**: Implemented a feature that allows rotating the image in 90° increments to correct the document's orientation.

---

# Contributing

If you'd like to contribute to this project, feel free to submit a pull request or open an issue on GitHub.
