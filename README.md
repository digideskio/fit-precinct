fit-precinct
=============================

Right now zhis is an implementation of the [openfitapi](openfitapi.com) specification for Google AppEngine. 
Despite its somewhat flawed design and being slightly outdated the API is supported by [IpBike](http://www.iforpowell.com/cms/index.php?page=ipbike).

I will create a working prototype that allows uploading data through IpBike (or any other application that implements openfitapi).
 
From there on i'll concentrate on the web frontend and - hopefully - add couple of simple charts and/or a map for analysis.

Once that skeleton app is done the aim is to put in an authentication handler and add multi-tenancy capability.

After that ...

 - Specify and implement an API for sharing data
 - allow sharing of data between instances of this app like <username>@<appinstancename>.appspot.com
 - create a better API and specification than openfitapi
 - user customizable charts
 - your idea?
 
General thoughts

 - I don't like to pay for evaluation of my data. And so shouldn't you. This code is licensed under the GNU General Public License. Feel free to use and extend.
 - I really suck at designing stuff so i'll use bootstrap for now
 
