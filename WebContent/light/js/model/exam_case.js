
/**
 * This class builds a local stub of the remote ExamCase.
 * @param {type} data1 JSON of ExamCase
 * @returns {ExamCase}
 */
function ExamCase(data1) {
    this.data = data1;
    this.allAdapters = new Array();
    this.allParts = {};
    this.currentIndex = 0;
    this.adapter = {};

    //Invoke initialization operations.
    buildExamCase(this, data1);


    this.nextQuestion = function () {
        this.currentIndex++;
        judgeAnswer(this.adapter);
        this.data.currentIndex = this.currentIndex;
        this.adapter = this.allAdapters[this.currentIndex];
    };
    this.previousQuestion = function () {
        this.currentIndex--;
        judgeAnswer(this.adapter);
        this.data.currentIndex = this.currentIndex;
        this.adapter = this.allAdapters[this.currentIndex];
    };

    /**
     * Invoke when submit the exam.
     * @returns {type}
     */
    this.submitExamCase = function () {
        var totalScore = 0.0;
        //Compute the total score.
        for (var i in this.allAdapters) {
            var ada = this.allAdapters[i];
            //console.log("Ada:"+JSON.stringify(ada));
            try {
                var tempScore = ada.realScore;
                if (!tempScore.isNaN) {
                    totalScore += tempScore;
                }
            } catch (err) {
                //console.log("Item:" + JSON.stringify(ada));
                continue;
            }
        }
        console.log("Total score:" + totalScore);
        this.data.score = totalScore;
        this.data.stat = "submitted";
        this.data.synchronized = false;
        //Return the data for futher process such as saving in local storage or posting to remote server.
        return this.data;
    };

    this.save = function () {
        saveExamCaseToLocal(this.data);
    };

}

/**
 * Check if the present adapter of the exam item is right, and set the details of the answering.
 * @param {type} adapter
 * @returns {undefined}
 */
function judgeAnswer(adapter) {
    //adapter.item.done = true;
    try {
        if (adapter.qtype === 'choice') {
            adapter.choiceItem.done = true;
            adapter.choiceItem.ifRight = (adapter.choiceItem.userAnswer === adapter.question.answer);
            adapter.choiceItem.rightAnswer = adapter.question.answer;
            if (adapter.choiceItem.ifRight) {
                adapter.choiceItem.realScore = adapter.choiceItem.score;
                adapter.realScore = adapter.choiceItem.realScore;
            }
            console.log("userAnswer:" + adapter.choiceItem.userAnswer + ",rightAnswer:" + adapter.question.answer + ",ifRight:" + adapter.choiceItem.ifRight);
        }
        if (adapter.qtype === 'mchoice') {
            var ansStr = "";
            var lenn = adapter.multiChoiceItem.userAnswers.length;
            if (lenn === null) {
                adapter.multiChoiceItem.userAnswer = '';
                return;
            } else {
                //console.log(adapter.multiChoiceItem.userAnswers);
            }
            for (var i in adapter.multiChoiceItem.userAnswers) {
                var str = adapter.multiChoiceItem.userAnswers[i];
                //console.log(str);
                ansStr += str;
            }
            adapter.multiChoiceItem.userAnswer = ansStr;
            adapter.multiChoiceItem.rightAnswer = adapter.question.answer;
            adapter.multiChoiceItem.ifRight = (adapter.multiChoiceItem.userAnswer === adapter.question.answer);
            if (adapter.multiChoiceItem.ifRight) {
                adapter.multiChoiceItem.realScore = adapter.multiChoiceItem.score;
                adapter.realScore = adapter.multiChoiceItem.realScore;
            }
            console.log("userAnswer:" + adapter.multiChoiceItem.userAnswer + ",rightAnswer:" + adapter.question.answer + ",ifRight:" + adapter.multiChoiceItem.ifRight);
        }
        if (adapter.qtype === 'fill') {
            //var tempStr=adapter;
            //console.log(JSON.stringify(tempStr));
            var uansStr = "";
            var ansStr = "";
            var total = 0;
            if (adapter.fillItem.blocks !== null) {
                total = adapter.fillItem.blocks.length;
            }
            var rightNum = 0;
            for (var i = 0; i < total; i++) {
                var str1 = adapter.fillItem.blocks[i].userAnswer;
                var str2 = adapter.fillItem.blocks[i].rightAnswer;
                if (str1 === str2) {
                    rightNum++;
                }
                uansStr += str1;
                ansStr += str2;
                if (i !== (total - 1)) {
                    uansStr += ",";
                    ansStr += ",";
                }
            }
            adapter.fillItem.rightNum = rightNum;
            var ratio = rightNum / total;
            if (ratio > 0.6) {
                adapter.fillItem.ifRight = true;
            }
            adapter.fillItem.realScore = ratio * adapter.fillItem.score;
            adapter.realScore = adapter.fillItem.realScore;
            adapter.fillItem.userAnswerStr = uansStr;
            adapter.fillItem.rightAnswerStr = ansStr;
            console.log("userAnswer:" + adapter.fillItem.userAnswerStr + ",rightAnswer:" + adapter.question.rightStr + ",ifRight:" + adapter.fillItem.ifRight);
        }
        if (adapter.qtype === 'judge') {
            adapter.judgeItem.ifRight = (adapter.judgeItem.userAnswer === adapter.question.answer);
            adapter.judgeItem.rightAnswer = adapter.question.answer;
            if (adapter.judgeItem.ifRight) {
                adapter.judgeItem.realScore = adapter.judgeItem.score;
                adapter.realScore = adapter.judgeItem.realScore;
            } else {
                adapter.realScore = 0;
            }
            console.log("userAnswer:" + adapter.judgeItem.userAnswer + ",rightAnswer:" + adapter.question.answer + ",ifRight:" + adapter.judgeItem.ifRight);
        }
        if (adapter.qtype === 'essay') {
            adapter.essayItem.ifRight = (adapter.essayItem.userAnswer === adapter.question.answer);
            adapter.essayItem.rightAnswer = adapter.question.answer;
            var ratio = compareStr(adapter.essayItem.userAnswer, adapter.essayItem.rightAnswer);
            if (ratio > 0.6) {
                adapter.essayItem.ifRight = true;
            }
            adapter.essayItem.realScore = adapter.essayItem.score * ratio;
            adapter.realScore = adapter.essayItem.realScore;
            console.log("userAnswer:" + adapter.essayItem.userAnswer + ",rightAnswer:" + adapter.question.answer + ",ifRight:" + adapter.essayItem.ifRight);
        }
        if (adapter.qtype === 'file') {
            adapter.fileItem.ifRight = (adapter.fileItem.userAnswer === adapter.question.answer);
            adapter.fileItem.rightAnswer = adapter.question.answer;
            var ratio = compareStr(adapter.fileItem.userAnswer, adapter.fileItem.rightAnswer);
            if (ratio > 0.6) {
                adapter.fileItem.ifRight = true;
            }
            adapter.fileItem.realScore = adapter.fileItem.score * ratio;
            adapter.realScore = adapter.fileItem.realScore;
            console.log("userAnswer:" + adapter.fileItem.userAnswer + ",rightAnswer:" + adapter.question.answer + ",ifRight:" + adapter.fileItem.ifRight);
        }
    } catch (e) {
        console.log(e);
    }
}
/** The End of the class. */

/** Below is the utility functions of ExamCase. **/
/**
 * Preprocessing for every adapter
 * @param {type} examCase
 * @param {type} data
 * @returns {undefined}
 */
function buildExamCase(examCase, data) {
    if (data === null) {
        return;
    }
    var ord = 1;
    var vparts = data.vparts;
    $(vparts).each(function (index1, part) {
        //console.log(part);
        examCase.allParts = vparts;
        var adapters = part.adapters;
        //console.log(adapters);
        $(adapters).each(function (index2, adapter) {
            adapter.partName = part.name;//Add 'partName' to adapter
            adapter.partId = part.id; //Add 'partId' to adapter
            if (adapter.qtype === 'choice') {
                adapter.score = part.choiceScore;
            }
            if (adapter.qtype === 'mchoice') {
                adapter.score = part.multiChoiceScore;
            }
            if (adapter.qtype === 'fill') {
                adapter.score = part.fillScore;
            }
            if (adapter.qtype === 'judge') {
                adapter.score = part.judgeScore;
            }
            if (adapter.qtype === 'essay') {
                adapter.score = part.essayScore;
            }
            if (adapter.qtype === 'file') {
                adapter.score = part.fileScore;
            }
            adapter.ord = ord;
            examCase.allAdapters.push(adapter);
            ord++;
        });

        //alert(index+":"+part.name);
    });
    //Set the current index
    if (data.currentIndex !== 0) {
        examCase.currentIndex = data.currentIndex;

    }
    examCase.adapter = examCase.allAdapters[examCase.currentIndex];
    console.log("handled.");
}


/**
 * Compare the similarity of two strings
 * @param {type} x
 * @param {type} y
 * @returns {Number}Return the samilarity
 */
function compareStr(x, y) {
    var z = 0;
    var s = x.length + y.length;

    x.sort();
    y.sort();
    var a = x.shift();
    var b = y.shift();

    while (a !== undefined && b !== undefined) {
        if (a === b) {
            z++;
            a = x.shift();
            b = y.shift();
        } else if (a < b) {
            a = x.shift();
        } else if (a > b) {
            b = y.shift();
        }
    }
    return z / s * 2;
}



/**
 * 将数据在本地异步保存
 * 考试详情的本地存储格式应该为：
 * {"id1":{examCase1},"id2":{examCase2}}
 * @param {type} examCase
 * @returns {undefined}
 */
function saveExamCaseToLocal(examCase) {
    var casesStr = localforage.getItem("rereexam.examcases").then(function (casesStr) {
        var cases = JSON.parse(casesStr);
        if (cases === null) {
            cases = {};
        }
        cases[examCase.id] = examCase;
        casesStr = JSON.stringify(cases);
        localforage.setItem("rereexam.examcases", casesStr).then(function (result) {

        });
    });
}
/**
 * 以promise规范在本地删除数据
 * @param {type} eid
 * @returns {unresolved}
 */
function deleteExamCaseFromLocal(eid) {
    var deferred = getDeferred();
    localforage.getItem("rereexam.examcases").then(function (casesStr) {
        //console.log(casesStr.indexOf(eid));
        var cass = JSON.parse(casesStr);

        //console.log(stat);
        delete cass[eid];
        var casesStr2 = JSON.stringify(cass);
        //console.log(casesStr2.indexOf(eid));

        localforage.setItem("rereexam.examcases", casesStr2).then(function (result) {
            deferred.resolve(casesStr2);
        });
    });
    return deferred.promise();
}
/**
 * 在promise规范在本地查询所有保存的考试
 * 以JSON格式返回本地保存的所有成绩详情
 * @returns {Array|Object}
 */
function findAllExamCasefromLocal() {
    var deferred = getDeferred();
    localforage.getItem("rereexam.examcases").then(function (data) {
        var cases = new Array();
        cases = JSON.parse(data);
        if (cases === null) {
            cases = {};
        }
        deferred.resolve(cases);
    });
    return deferred.promise();
}

/**
 * 以promise规范以数组格式封装所有考试数据
 * 以数组格式返回本地保存的所有成绩详情
 * @returns {Array|findAllExamCasefromLocalAsArray.ess}
 */
function findAllExamCasefromLocalAsArray() {
    var deferred = getDeferred();
    findAllExamCasefromLocal().then(function (cases) {
        var ess = new Array();
        for (var prop in cases) {
            if (cases.hasOwnProperty(prop)) {
                ess.push(cases[prop]);
                //console.log('key is ' + prop + ' and value is' + cases[prop]);
            }
        }
        ess.reverse();
        deferred.resolve(ess);
    });
    return deferred.promise();
}

/**
 * 以promise规范封装单条查询的成绩数据
 * 从本地获取一个成绩详情
 * @param {type} caseId
 * @returns {unresolved}
 */
function findExamCasefromLocal(caseId) {
    var deferred = getDeferred();
    localforage.getItem("rereexam.examcases").then(function (casesStr) {
        var cc = null;
        var cases = new Array();
        cases = JSON.parse(casesStr);
        if (cases !== null) {
            cc = cases[caseId];
        }
        deferred.resolve(cc);
    });
    return deferred.promise();
}

/**
 * 本地考试成绩与远程考试成绩合并，若远程的成绩在本地不存在，并且远程成绩为已提交状态，则加入本地列表；
 * 若远程成绩在本地不存在，并且状态为未提交，则忽略该成绩信息。即在其它终端未完成考试不能在本地续考
 * @param {type} cases1 本地成绩
 * @param {type} cases2 远程成绩
 * @returns {Array}
 */
function mergeExamCases(cases1, cases2) {
    if (cases1 === null) {
        cases1 = [];
    }
    //console.log("me:"+JSON.stringify(cases2));
    for (var ii in cases2) {
        try {
            var exists = false;
            var case2 = cases2[ii];
            var case1;
            for (var jj in cases1) {
                //console.log("me:"+JSON.stringify(jj));
                case1 = cases1[jj];
                if (case1.id === case2.id) {
                    console.log("me:"+case2);
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                //console.log(case2);
                if (case2.stat === 'submitted') {
                    cases1.push(case2);
                }
            }
        } catch (e) {
            console.log(e);
        }

    }
    return cases1;
}

function saveExamLabels(labels) {
    localforage.setItem("rereexam.examlabels", labels).then(function (result) {

    });
}
function findExamLabelfromLocal(lid) {
    var deferred = getDeferred();
    localforage.getItem("rereexam.examlabels").then(function (labels) {
        for (var li in labels) {
            var label = labels[li];
            try {
                if (lid === label.id) {
                    deferred.resolve(label);
                    break;
                }
            } catch (e) {
                console.log(e);
            }
        }
        deferred.resolve(null);
    });
    return deferred.promise();
}

function findAllExamLabelsfromLocal() {
    var deferred = getDeferred();
    localforage.getItem("rereexam.examlabels").then(function (labels) {
        deferred.resolve(labels);
    });
    return deferred.promise();
}