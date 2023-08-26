# WhatsNextSeries
This is an multiplatform app.
WhatsNext is using a service with json requests to get tv shows and theirs 
schedules from API provided by [TheMovieDB] (https://www.themoviedb.org/).

## How to setup the TheMovieDB api key

* The api key is stored in a environment variable and is retrieve using generated code during desing/build time. 

* Create a environment variable called `TheMovieDbApiKey` with the value of your api key from [TheMovieDB](https://www.themoviedb.org/).

* You can check in WhatsNextSeries.Services package, in the generated file TheMovieDbApiKey.cs, if your api key was set up correctly.

    * If you are using Visual Studio or Rider, you need to restart them to get the new environment variable. 
    * If the problem persists, try restart the computer.

### License

```
   Copyright 2016 Adley Wollmann Damaceno

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
