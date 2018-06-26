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
        tmpLeft = []
        tmpRight = []
        for x in value1.unit:
            tmpLeft.append(unitDec(x.unit,x.pot))
        for x in value2.unit:
            tmpRight.append(unitDec(x.unit,x.pot))
        tmp = tmpLeft + tmpRight
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
        tmpLeft = []
        tmpRight = []
        for x in value1.unit:
            tmpLeft.append(unitDec(x.unit,x.pot))
        for x in value2.unit:
            tmpRight.append(unitDec(x.unit,x.pot))
        for x in tmpRight:
            x.pot = -x.pot
        tmp = tmpLeft + tmpRight
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
        value = value1.value / value2.value
        return val(value,tmp)

    def multiply_simp(value1,value2):
        return val(value1.value * value2,value1.unit)

    def divide_simp(value1,value2):
        return val(value1.value / value2,value1.unit)

    def add_simp(value1,value2):
        return val(value1.value + value2,value1.unit)

    def sub_simp(value1,value2):
        return val(value1.value - value2,value1.unit)


    def greater(value1,value2):
        if(value1.value > value2.value):
            return True
        return False

    def lesser(value1,value2):
        if(value1.value < value2.value):
            return True 
        return False    

    def equalTo(value1,value2):
        if(value1.value == value2.value):
            return True
        return False
            
        
    def printVal(value):
        tmp = str(value.value) + " "
        neg = []
        pos = []
        for x in value.unit:
            if(x.pot > 0):
                pos.append(x)
            if(x.pot < 0):
                neg.append(x)
        units = ""
        if(len(pos)==0 and len(neg) != 0):
            units += "1"
        elif(len(pos)==0):
            units = ""
        else:
            for x in pos:
                units += x.unit + "^" + str(x.pot)
        if(len(neg)!=0):
            units += "/"
            for x in neg:
                units += x.unit + "^" + str(x.pot)
        tmp += units
        return tmp