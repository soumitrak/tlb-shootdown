compile:
	mvn install

cdf:
	-mkdir data
	mvn exec:java -Dexec.mainClass="com.sk.CreateDataFiles" -Dexec.args="-p data/data- -nf 50000"

br:
	-grep TLB /proc/interrupts
	mvn exec:java -Dexec.mainClass="com.sk.BufferPoolReader" -Dexec.args="-p data/data- -nf 50000 -nt 12 -nr 100"
	-grep TLB /proc/interrupts

mr:
	-grep TLB /proc/interrupts
	mvn exec:java -Dexec.mainClass="com.sk.MMapReader" -Dexec.args="-p data/data- -nf 50000 -nt 12 -nr 100"
	-grep TLB /proc/interrupts

clean:
	rm -rf data
	mvn clean

