# QFLan
A framework for quantitative modeling and analysis of highly (re)configurable systems

<img class=" alignright" src="https://github.com/qflanTeam/QFLan/blob/master/logo.png" alt="QFLAN" width="200" height="184" />

<h1>Quick Links</h1>
<ul>
 	<li><a href="https://github.com/qflanTeam/QFLan/wiki">QFLAN main page</a></li>
 	<li><a href="https://github.com/qflanTeam/QFLan/wiki/Install-QFLan">Install QFLAN</a></li>
 	<li><a href="https://github.com/qflanTeam/QFLan/wiki/Sample-models">Sample models</a></li>
</ul>

<h1>Dsclaimer: this page has been anonymized for double-blindness constraints</h1>


<h1>Summary</h1>
<p style="text-align: justify;"><em>QFLan</em> is a software tool for the modeling and analysis of highly reconfigurable systems, including software product lines.</p>
<p style="text-align: justify;">The tool offers an easy-to-use, rule-based probabilistic language to specify models with probabilistic behaviour. Quantitative constraints can be used to restrict the class of admissible configurations (or products), like (using a family of reconfigurable vending machines from <a href="https://www.dropbox.com/s/hkxyfn4cp6uar7o/fase_2018.pdf?dl=1">here</a>):
</p>

<ul>
 	<li style="text-align: justify;">machines can have a certain maximum cost,</li>
 	<li style="text-align: justify;">machines serving coffee-based beverages cannot sell tea,</li>
 	<li style="text-align: justify;">in order to serve cappuccino it is necessary to have the feature of serving also coffee,</li>
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
