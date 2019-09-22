## Introduction

Android baking application that demonstrates the use of widgets, ExoPlayer, handling errors within
imperfect JavaScript Object Notation (JSON), and limited offline functionality making use of
database storage.

# Overview

This app uses the internet to retrieve a JSON file containing various recipes, with the
understanding that this file may change at any time, but the endpoint will stay the same.

Each recipe consists of a list of ingredients (including quantities) and instructions. Many of the
steps include a link to a movie or else an image. At the moment, no images are actually linked, but
this code handles the case when they will be added (which is potentially never, given the nature of
the endpoint). In practice, you could provide your own JSON file (and endpoint to retrieve from) if
you keep the same fields, and you would have a functioning app for displaying your own recipes and
instructions.

## Getting started / Usage

The files for this project were built using Android Studio, so you will likely have the easiest
time duplicating the original behavior using the same.

 1. Clone this repository
    * git clone https://github.com/munifrog/BakingFacilitator.git
 1. Open this directory using Android Studio
 1. Launch the app on an emulator or device that has internet access

# License
This project started as a task within the [Udacity Android Developer Nanodegree Course](https://www.udacity.com/course/android-developer-nanodegree-by-google--nd801)
and Udacity (or Google) owns the rights for the project idea.

While I personally prefer indirect sources for finding solutions to puzzles, you may learn from
what I have done, if that helps. Just be careful to avoid plagiarism if you are also taking this
course and looking for answers.

If, on the other hand, you want to build on what I have done, that is also welcome.