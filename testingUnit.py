import itertools
import sys
 
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
    
    def add(value1,value2):
        check = True
        for x in value1.unit:
            for y in value2.unit:
                check = False
                if(x.unit == y.unit and x.pot == y.pot):
                    check = True
                    break
        if check:
            unit = value1.unit
            value = value1.value + value2.value
        else:
            print("ERROR: Not able to sum values with different units")
            sys.exit()   
        return val(value,unit) 

    def sub(value1,value2):
        check = True
        for x in value1.unit:
            for y in value2.unit:
                check = False
                if(x.unit == y.unit and x.pot == y.pot):
                    check = True
                    break
        if check:
            unit = value1.unit
            value = value1.value - value2.value
        else:
            print("ERROR: Not able to subtract values with different units")
            sys.exit()   
        return val(value,unit) 

    def multiply(value1,value2):
        tmp = value1.unit + value2.unit
        for x in value1.unit:
            for y in value2.unit:
                if x.unit == y.unit and x.pot == y.pot:
                    for z in tmp:
                        if z.unit == x.unit and z.pot == x.pot:
                            tmp.remove(z)
                            break            
                    for z in tmp:
                        if z.unit == x.unit and z.pot == x.pot: 
                            z.pot = z.pot+x.pot

        for z, m in itertools.combinations(tmp, 2):
            if(z.unit == m.unit and -z.pot == m.pot):
                if(m in tmp and z in tmp):
                    tmp.remove(z)
                    tmp.remove(m)
            elif(z.unit == m.unit and z.pot > m.pot and m.pot < 0):
                if(m in tmp):
                    z.pot = z.pot + m.pot
                    tmp.remove(m)                 
            elif(z.unit == m.unit and ((z.pot < 0 and m.pot < 0) or (z.pot > 0 and m.pot > 0))):
                if(m in tmp):
                    z.pot = z.pot + m.pot
                    tmp.remove(m)    
        value = value1.value * value2.value
        return val(value,tmp) 
    
    def divide(value1,value2):
        for x in value2.unit:
            x.pot = -x.pot
        tmp = value1.unit + value2.unit
        for x in tmp:
            print (x.unit + str(x.pot))
        for z, m in itertools.combinations(tmp, 2):
            print(z.unit + "("+ str(z.pot) +")" + m.unit + "("+ str(m.pot) +")")
            if(z.unit == m.unit and -z.pot == m.pot):
                if(m in tmp and z in tmp):
                    tmp.remove(z)
                    tmp.remove(m)
            elif(z.unit == m.unit and z.pot > m.pot and m.pot < 0):
                if(m in tmp):
                    print("ola")
                    z.pot = z.pot + m.pot
                    tmp.remove(m)                 
            elif(z.unit == m.unit and ((z.pot < 0 and m.pot < 0) or (z.pot > 0 and m.pot > 0))):
                if(m in tmp):
                    print("adeus")
                    z.pot = z.pot + m.pot
                    tmp.remove(m)
        value = value1.value / value2.value
        return val(value,tmp)                 
        
    def printVal(value):
        tmp = str(value.value) + " "
        for x in value.unit:
            if(x.pot == 1):
                tmp = tmp + x.unit
            if(x.pot > 1 and x.pot != 0):
                tmp = tmp + x.unit
                tmp = tmp + "^" + str(x.pot)
            elif(x.pot < 0):
                tmp = tmp + "/" + str(x.unit)
                if(-x.pot > 1):
                    tmp = tmp + "^" + str(-x.pot)
        return tmp 