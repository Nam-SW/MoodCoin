const express = require('express');
const app = express();
const bodyParser = require('body-parser');
const mysql = require('mysql');
const mysqlsync = require('sync-mysql');
const delay = require('delay');

var sendperson, getperson, sendprice, gethavemoney, usermoney;
var sendhavemoney, sendprice;
var db = mysql.createConnection({
    host: "localhost",
    database: "moodpayment",
    user: "root",
    password: "0000"
})

var syncdb = new mysqlsync({
    host: "localhost",
    database: "moodpayment",
    user: "root",
    password: "0000"
})

db.connect((error) =>{
    if(error){
        throw(error);
    }
    else{
        console.log("database connected");
    }
});

app.use(bodyParser.json())

app.post('/', (req, res) =>{
    console.log("머기")
    valueclear()
    console.log(req.body);
    console.log(typeof(req.body))
    sendperson = req.body.sendperson;
    console.log(sendperson);
    db.query(`SELECT money FROM user WHERE id = ${sendperson}`, function(err, docs){
        if(err){
            throw err;
        }
        usermoney = 0;
        if(JSON.stringify(docs) == '[]'){
            console.log(typeof(JSON.stringify(docs)));
            console.log("값이 없나?");
            db.query(`INSERT INTO user (id, money) VALUES(${sendperson}, 0)`, function(err, result, fields){
                if(err){
                    throw err;
                }
                console.log("삽입");
                res.send('0');
            }); f
        }
        else{
            console.log("돈확인:", docs);
            usermoney = docs[0].money;
            console.log("들어갔다.");
            console.log("money type", typeof(usermoney))
            res.send(usermoney.toString());
        }
    });      
});

app.post('/pay', (req, res) =>{
    console.log("머기중");
    valueclear()
    console.log(req.body);
    console.log(typeof(req.body))
    sendperson = req.body.sendperson;
    getperson = req.body.getperson;
    sendprice = Number(req.body.sendprice);
    
    let docs = syncdb.query(`SELECT money From user Where id = ${sendperson}`);
    console.log("테스트:", docs);
    sendhavemoney = Number(docs[0].money);
    console.log("보내는친구 돈가져올게 :", sendhavemoney );
 
    docs = syncdb.query(`SELECT money From user Where id = ${getperson}`);
    console.log("docs : ", docs);
    if(JSON.stringify(docs) == '[]'){
        console.log(typeof(JSON.stringify(docs)));
        console.log("친구가 없나?");
        res.end("친구 정보 없어여");
        return ;    
    }else{
        gethavemoney = Number(docs[0].money);   
        console.log("받는친구 돈가져올게 : ", gethavemoney);
        console.log(typeof(gethavemoney))
    }

    if(sendhavemoney - sendprice < 0) res.end("잔액 부족인디?");
    else{
        console.log("확인");
        console.log("sendhavemoney :", sendhavemoney);
        console.log("gethavemoney : ", gethavemoney);
        console.log("sendprice: ", sendprice);

        db.query(`UPDATE user SET money = ${gethavemoney + sendprice} WHERE id = ${getperson}`, function(err, docs){
            if(err){
                throw err;
            }
            console.log("받는 친구 돈 줄게");  
        });

        db.query(`UPDATE user SET money = ${sendhavemoney - sendprice} WHERE id = ${sendperson}`, function(err, docs){
            if(err){
                throw err;
            }
            console.log("주는 친구 돈 가져갈게");
        });
        res.end(`${sendhavemoney - sendprice}`);
    }
    console.log("처리 완료");
    
});

app.post('/mywallet', (req, res) =>{
    console.log("mywallet 접속");
    valueclear()
    sendperson = req.body.sendperson;
    db.query(`SELECT money FROM user WHERE id = ${sendperson}`, function(err, docs){
        if(err){
            throw err;
        }
        if(JSON.stringify(docs) == '[]'){
            console.log(typeof(JSON.stringify(docs)));
            console.log("값이 없나?");
            db.query(`INSERT INTO user (id, money) VALUES(${sendperson}, 0)`, function(err, result, fields){
                if(err){
                    throw err;
                }
                console.log("삽입");
                res.send('0');
            });
        }
        else{
            console.log("돈확인:", docs);
            usermoney = docs[0].money;
            console.log("money tpye", typeof(usermoney))
            res.send(usermoney.toString());
        }
    });    
});

app.post('/statemoney', (req, res) =>{
    console.log("statemoney 접속");
    valueclear()
    sendperson = req.body.sendperson;
    var addprice = Number(req.body.addprice);
    console.log(typeof(sendperson), "  ", typeof(addprice))
    console.log(sendperson, addprice);
    if(addprice == null) addprice = 0;
    db.query(`SELECT money FROM user WHERE id = ${sendperson}`, function(err, docs){
        if(err){
            throw err;
        }
        if(JSON.stringify(docs) == '[]'){
            console.log(typeof(JSON.stringify(docs)));
            console.log("값이 없나?");
            usermoney = 0;
            db.query(`INSERT INTO user (id, money) VALUES(${sendperson}, 0)`, function(err, result, fields){
                if(err){
                    throw err;
                }
                console.log("삽입");
                res.send('0');
                return ;
            });
        }
        else{
            console.log("돈확인:", docs);
            usermoney = docs[0].money;
            console.log("money tpye", typeof(usermoney))
        }

        console.log(`usermoney : ${usermoney}`);
        console.log(`addprice : ${addprice}`);
        console.log(`UPDATE user SET money = ${usermoney + addprice} WHERE id = ${sendperson}`);
        db.query(`UPDATE user SET money = ${usermoney + addprice} WHERE id = ${sendperson}`, function(err, docs){
            if(err){
                throw err;
            }
            console.log("돈 적립");  
            res.send((usermoney + addprice).toString());
        });
    });    
});

app.listen(3000, () =>{
    console.log("서버 포트 3000번으로 오픈했습니다");
});
 
function valueclear(){
    console.log("valueclear")
    sendperson = 0;
    getperson = 0; 
    sendprice = 0;
    gethavemoney = 0;
    usermoney = 0;
}