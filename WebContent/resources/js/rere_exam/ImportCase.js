/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function ImportCase() {
    this.choiceNum = 0;
    this.choiceQuestions = new Array();
    this.lines = new Array();//记录有效文本行

    //以下开始定义方法--------------

    this.parse = function (cnt) {
        cnt = cnt.replace(/\s+/g,'');
        var lns = cnt.split('<br/>');
        this.lines.length=0;//清空数组
        for (var i = 0; i < lns.length; i++) {
            var ln = lns[i];
            if (ln != null) {
                ln = jQuery.trim(ln);
                if (ln != '') {
                    this.lines.push(ln);
                }
            }
        }
        return this.lines;
    };

    this.read = function () {
        var len = this.lines.length;
        if (len != 0) {
            this.choiceQuestions.length=0;//清空数组
            var start = 0;
            while (true) {//循环读取题目
                var choice = new ChoiceQuestion();
                var currentIndex = choice.read(this.lines, start, len);
                //console.log(currentIndex);
                if (choice.ifRead) {//如果读到了题目，则加入集合
                    this.choiceQuestions.push(choice);
                }

                //结尾处验证本轮读取进展
                if (currentIndex == start) {//本轮未读到题目，退出循环
                    break;
                } else {//本轮读到了题目，将新的起始位置设置为当前游标
                    start = currentIndex;
                }
            }
            this.choiceNum=this.choiceQuestions.length;
        }
    };


}


