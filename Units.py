from unitClass import unitDec
class prog1:
    UnitDic = {}
    v1 = unitDec("lfa",1)
    UnitDic['lfa'] = v1
    v2 = unitDec("ee",1)
    UnitDic['ee'] = v2
    v3 = unitDec("mt",1)
    UnitDic['muito'] = v3
    v4 = unitDec("fx",1)
    UnitDic['fixe'] = v4
    v5 = unitDec(v1.unit,v1.pot)
    v6 = [v5]
    v7 = unitDec(v2.unit,v2.pot)
    v8 = [v7]
    v9 = v6 + v8
    v10 = unitDec(v3.unit,v3.pot)
    v11 = [v10]
    v12 = unitDec(v4.unit,v4.pot)
    v13 = [v12]
    for x in v13:
        x.pot = -x.pot
    v14 = v11 + v13
    v15 = v9 + v14
    v16 = v15
    UnitDic['obgMos'] = v16
    v1.pot = 2