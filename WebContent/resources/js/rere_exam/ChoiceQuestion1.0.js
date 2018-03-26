/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function ChoiceQuestion() {
    this.id;
    this.content = "";
    this.choices = new Array();
    this.answer = "";
    this.ifRead = false;


    this.addChoice = function (choice) {
        this.choices.push(choice);
    };

    //以下为本类的方法-------------------

    /**
     * 
     * @param {type} lines
     * @param {type} start
     * @param {type} end
     * @returns {undefined}
     */
    this.read = function (lines, start, end) {
        start = arguments[1] ? arguments[1] : 0;//设置参数start的默认值为0
        end = arguments[2] ? arguments[2] : lines.length;//设置参数end的默认值为lines数组长度
        var ifReadChoice = false;
        try {
            //console.log('Start to read choice question.');
            var str = '';//暂存试题内容
            for (var i = start; i < end; i++) {
                
                var line = lines[i];
                //console.log(line+":"+line.indexOf('答案'));
                line = jQuery.trim(line);
                line=line.replace(/[\u200B-\u200D\uFEFF]/g, '');
                if (line.indexOf('单选题') == 0) {
                    continue;
                }

                if (line.toUpperCase().indexOf('A') == 0) {//读选项开始
                    ifReadChoice = true;
                    for (var j = 0; j < 6; j++) {
                        var label = String.fromCharCode('A'.charCodeAt(0) + j);//以A为起点，加数字后变为B C D E
                        //console.log(label);
                        line = lines[i+j];
                        if (line.toUpperCase().indexOf(label) == 0) {
                            var c = new RereChoice();
                            c.label = label;
                            var cnt = line.replace(label + '、', '');
                            cnt = cnt.replace(label + '.', '');
                            c.content = jQuery.trim(cnt);
                            this.addChoice(c);
                        } else {
                            break;
                        }
                    }
                    //i=i+this.choices.length-1;
                } else if (line.indexOf('答案')!=-1) {
                    var s=line.indexOf('答案')+2;
                    line=line.substring(s,line.length);
                    //console.log("substring:"+line)
                    
                    var ans = line.replace('：', '');
                    ans = ans.replace(':', '');
                    ans = jQuery.trim(ans);
                    ans=ans.replace(/[\u200B-\u200D\uFEFF]/g, '');;
                    //var yg=line.charAt(0);
                    //console.log("yg:"+yg);
                    console.log('Answer:'+ans+",len:"+ans.length);
                    if (ans.length!=1) {//若答案长度不为2，则说明不是单选题，应返回初始读取位置,该长度包括换行符
                        return start;
                    }
                    this.answer = ans;
                } else {
                    if (!ifReadChoice){//选项没有读取时才进行此操作
                        str += line;//若读不到选项，则段落认为是试题内容
                    }
                }
                if(ifReadChoice && str != '' && this.answer != ''){//选项、试题内容、答案都读取完成，则本题读取结束
                    break;
                }
            }
            this.content = str;
            //console.log(ifReadChoice+this.content+this.answer);
            if (ifReadChoice && this.content != '' && this.answer != '') {//选项、试题内容、答案都正确读取
                this.ifRead = true;
                return i+1;//返回当前已经读到的行位置
            }
        } catch (e) {
            console.log(e);
        }
        return start;
    };

}



