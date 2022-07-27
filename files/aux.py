devices = open("./files/devices_test2.txt","w")
people = open("./files/people_test2.txt","w")
houses = open("./files/houses_test2.txt","w")


# Houses
# Morada(string);[(Room:[d1.d2.d2])];NIF(int);Fornecedor(string)
# 
# 
# People 
# Name(string);NIF(int)
bools = ["true","false"]
brands = ["Sony","Samsung","Xiaomi","Apple","Asus","LG","Acer","OnePlus"]
length_b = len(brands)
# generate devices
# SmartBulb;ID(string);ON/OFF(bool);TONE(int);DIMENSION(double)
# SmartSpeaker;ID(string);ON/OFF(bool);Volume(int);Channel(string);Marca(string)
# SmartCamera;ID(string);ON/OFF(bool);resx(int);resy(int);sizeoffile(float)
for i in range(1,801):
    devices.write("SmartBulb;b{0};{1};1;10\n".format(i,bools[i%2]))
    devices.write("SmartSpeaker;s{0};{1};10;Channel{0};{2}\n".format(i,bools[(i+1)%2],brands[i%length_b]))
    devices.write("SmartCamera;c{0};{1};1080;1920;100\n".format(i,bools[i%2]))

# generate people
# Name;NIF
for i in range(1,161):
    people.write("Person{0};{0}\n".format(i))

providers = ["EDP","Endesa","GoldEnergy","Iberdrola","Galp","Coopernico","Enat","YIce","MEO Energia","Muon","Luzboa","Energia Simples","SU Electricidade","EDA"]
length_p = len(providers)
# generate houses
# Morada(string);[(Room:[d1.d2.d2])];NIF(int);Fornecedor(string)
for i in range(1,161):
    houses.write("Street{};[".format(i))
    houses.write("(Sala:[")
    for j in range(5*i-4,5*i-1):
        houses.write("b{0}.s{0}.c{0}.".format(j));
    houses.seek(houses.tell()-1,0);
    houses.write("]),(Quarto:[")
    for j in range(5*i-1,5*i+1):
        houses.write("b{0}.s{0}.c{0}.".format(j))
    houses.seek(houses.tell()-1,0);
    houses.write("])];")
    houses.write("{0};{1}\n".format(i,providers[i%length_p]))

devices.close()
people.close()
houses.close()
print("Done!")