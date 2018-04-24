# QFLan
A framework for quantitative modeling and analysis of highly (re)configurable systems

<img class=" alignright" src="https://github.com/qflanTeam/QFLan/blob/master/logo.png" alt="QFLAN" width="200" height="184" />

<h1>Please visit <a href="https://github.com/qflanTeam/QFLan/wiki">QFLAN main page</a>.</h1>

<!--
<h1>Quick Links</h1>
<ul>
 	<li><a href="https://github.com/qflanTeam/QFLan/wiki">QFLan main page</a></li>
 	<li><a href="https://github.com/qflanTeam/QFLan/wiki/Install-QFLan">Install QFLan</a></li>
 	<li><a href="https://github.com/qflanTeam/QFLan/wiki/Models-from-TSE-submission">Models from TSE submission</a></li>
 	<li><a href="https://github.com/qflanTeam/QFLan/wiki/Publications">Publications</a></li>
 	<li><a href="https://github.com/qflanTeam/QFLan/wiki/Sample-models">Sample models</a></li>
 	<li><a href="https://github.com/qflanTeam/QFLan/wiki/Source-code">Source code</a></li>
</ul>
-->



<h1>Summary</h1>
<p style="text-align: justify;"><em>QFLan</em> is a software tool for the modeling and analysis of highly reconfigurable systems, including software product lines.</p>
<p style="text-align: justify;">The tool offers an easy-to-use, rule-based probabilistic language to specify models with probabilistic behaviour. Quantitative constraints can be used to restrict the class of admissible configurations (or products), like (using a family of reconfigurable vending machines from <a href="https://www.dropbox.com/s/tdclnizprfytpg0/fm18%2Btool.pdf?dl=1">here</a>):
</p>

<ul>
 	<li style="text-align: justify;">machines can have a certain maximum cost,</li>
 	<li style="text-align: justify;">machines serving coffee-based beverages cannot sell tea,</li>
 	<li style="text-align: justify;">in order to serve cappuccino it is necessary to have the feature of serving also coffee,</li>
</ul>

Also it is possible to express conditions like:
<ul>
  	<li style="text-align: justify;">machines serving cappuccino provided with a coca dispenser can serve chocaccino.</li>
</ul>

<p style="text-align: justify;">QFLan has been combined with the distributed statistical model checker <a href="http://sysma.imtlucca.it/tools/multivesta/">MultiVeStA</a> to perform efficient quantitative analyses, including questions like:</p>

<ul>
 	<li style="text-align: justify;">average cost of machines (at varying of time);</li>
 	<li style="text-align: justify;">probability of installing certain features;</li>
 	<li style="text-align: justify;">probability of dumping a machine.</li>
</ul>
The tool is provided with a modern integrated development environment built using <a href="https://eclipse.org/Xtext/">XTEXT</a> technology, featuring high-level model and property specification languages.
<p style="text-align: justify;"><img class="aligncenter" src="https://github.com/qflanTeam/QFLan/blob/master/schreenshotMachineLabelled.png" alt="QFLan" /></p>


For further information please visit <a href="https://github.com/qflanTeam/QFLan/wiki">QFLAN main page</a>.

<h1>About</h1>
For suggestions, remarks, bugs or requests please do not hesitate to contact any of us (in alphabetical order):

<ul>
 	<li>axel.legay@inria.fr</li>
 	<li>albl@dtu.dk</li>
 	<li>maurice.terbeek@isti.cnr.it</li>
  <li>anvan@dtu.dk</li>
</ul>
