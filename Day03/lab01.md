* Here's a string input that contains several numbers separated by single or double spaces

``` java
	String input = "409  194  207  470  178  454  235  333  511  103  474  293  525  372  408  428 4321  2786  6683  3921  265  262  6206  2207  5712  214  6750  2742  777  5297 3536  2675  1298  1069  175  145  706  2614  4067  4377  146  134  1930  3850 2169  1050  3705  2424  614  3253  222  3287  3340  2637  61  216  2894  247 214 99  797  80  683  789  92  736  318  103  153  749  631  626  367  110  805 2922  1764  178  3420  3246  3456  73  2668  3518  1524  273  2237  228  1826  182 4682  642  397  5208  136  4766  180  1673  1263  4757  4680  141  4430  1098  188 158  712  1382  170  550  913  191  163  459  1197  1488  1337  900  1182  1018  337 3858  202  1141  3458  2507  239  199  4400  3713  3980  4170  227  3968  1688  4352 4168 209";
```

* Your application will print a checksum 
* Checksum is computed as product of the sum of the digits :)
* If your input is __409  194  207  470__
* Checksum is __4+0+9 * 1+9+4 * 2+0+7 * 4+7+0__ which is the product of __13 * 14 * 9 * 11__

* Implement a __Coordinator actor__ that serves as a guardian
* The Coordinator reads the input and generates a list of the numbers
* The sum of the digits of each number is computed by a __Sum actor__
* Coordinator maintains a router of Sum actors; Router can be random or roundrobin 
* It fires all the numbers to the router
* __Sum actor__ returns the sum of the digits to the coordinator actor which finally displays the checksum
 