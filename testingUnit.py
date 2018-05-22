import quantities as pq


class tryHard:
    Dic = {}
    Dic['kg'] =  pq.kg   

class unitDec:
    def __init__(self,unit,pot):
        self.unit = unit
        self.pot = pot
    def toString(unit):
        tmp = unit.unit
        if(unit.pot > 1 ):
            tmp = tmp + "^" + str(unit.pot)
        return tmp 


class val:
    def __init__(self, value,unit):
        self.value = value
        if(isinstance(unit,(list,))):
            unitList = unit
        else:
            unitList = []
            unitList.append(unit)
        self.unit = unitList
    def multiply(value1,value2):
        tmp = list(set().union(value1.unit,value2.unit))
        for x in value1.unit:
            for y in value2.unit:
                if x.unit == y.unit and x.pot == y.pot:
                    for z in tmp:
                        if z.unit == x.unit and z.pot == x.pot:
                            tmp.remove(z)
                            break            
                    for z in tmp:
                        if z.unit == x.unit and z.pot == x.pot:
                            z.pot = z.pot+1
        value = value1.value * value2.value
        return val(value,tmp)    
    def printVal(value):
        tmp = str(value.value)
        for x in value.unit:
            tmp = tmp + x.unit
            if(x.pot > 1 ):
                tmp = tmp + "^" + str(x.pot)
        return tmp 


triun = unitDec("kg",1)
triun2 = unitDec("kg",1)
triun3 = unitDec("coco",2)
triun4 = unitDec("popo",4)
unitList = [triun,triun4]
unitList2 = [triun3,triun2]

tried = val(2,triun)
trei = val(2,triun2)
newVal = val(3,triun3)

newvar = val.multiply(tried,trei)
for x in newvar.unit:
    print(x.unit)

print("---------------------------------")
newvar2 = val.multiply(newvar,newVal)    
for x in newvar2.unit:
   print(x.unit)

print("---------------------------------")
newVal2 = val(4,unitList)
newVal3 = val(5,unitList2)
newvar3 = val.multiply(newVal2,newVal3)
for x in newvar3.unit:
    print("Unidade: " + x.unit)
    print("Pot: " + str(x.pot))
print("---------------------------------")
newun = unitDec("coco",2)
newVal4 = val(4,newun)
newvar4= val.multiply(newvar3,newVal4)
for x in newvar4.unit:
    print(x.unit)
    print(x.pot)
print(val.printVal(newvar4))