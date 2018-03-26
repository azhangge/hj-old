

/**
 * Examination Page
 */
examModule.controller("examCaseCtr", function ($scope, $http, $state, $stateParams, $ionicPopup) {
    $scope.examCase = {};
    $scope.loading = true;
    $scope.result;
    $scope.countDownStr = "0分0秒";
    var eid = $stateParams.eid;
    var case_id = $stateParams.case_id;
    //console.log("eid:"+eid);

    $scope.init1 = function () {
        findExamCasefromLocal(eid).then(function (datax) {
            //console.log("local data:"+datax);
            if (!datax) {
                var uurl = contextPath + "/json/exam_case.jsp?eid=" + eid + "&uid=" + uid;
                //console.log(uurl);
                $http.get(uurl).success(function (data) {
                    $scope.data = data;
                    //console.log("data from server:" + data);
                    //本地异步保存
                    $scope.examCase = new ExamCase(data);
                    $scope.loading = false;
                    $scope.beginCount();
                });
            } else {
                console.log("error");
                $scope.examCase = new ExamCase(datax);
                $scope.loading = false;
                $scope.beginCount();
                $scope.$apply();
            }
        });
    };

    
    $scope.init1();


    $scope.save = function () {
        var sid = "rereexam.examcases." + $scope.examCase.data.id;
        //本地异步保存
        $scope.examCase.save();
        var alertPopup = $ionicPopup.alert({
            title: '提示',
            template: '保存成功！'
        });
    };
    $scope.submit = function () {
        //console.log("Submit begin.");
        var datax = $scope.examCase.submitExamCase();
        //console.log("Submit OK.");
        $scope.examCase.save();
        var postData = {};
        postData.examcase = JSON.stringify($scope.examCase.data);
        postData.uid = uid;
        //console.log("post:" + JSON.stringify(postData));
        var purl = contextPath + "/json/exam_case_post.jsp";
        $.post(purl, postData, function (data) {
            if (data !== null) {
                data = data.replace(" ", "");
                $scope.result = data;
                console.log("Results data: " + data);
            }
            if (data === "success") {
                $scope.examCase.data.synchronized = true;
                $scope.examCase.save();
            }
            //$state.go("Default", {}, {reload: true});
        });
        clearInterval(timer);
        var params = JSON.stringify($scope.examCase.data);
        //console.log("data:"+params);
        $state.go("ExamCaseResult", {data: params}, {reload: true});
    };
    var timer = null;
    var maxtime = 0;
    var passtime = 0;
    var altered = false;
    $scope.countDown = function (max, passed)
    {
        if (max >= 0)
        {
            var minutes = Math.floor(max / 60);
            var seconds = Math.floor(max % 60);
            var msg = minutes + " 分 " + seconds + " 秒";
            $scope.countDownStr = msg;
            //console.log("Counting..."+$scope.countDownStr);
            maxtime--;
            passtime++;
            $scope.examCase.data.timePassed = passtime; //
            $scope.$apply();
        } else
        {
            alert('交卷时间已到，点[确定]后试卷将自动提交！');
            //jQuery("#stepPage\\:myForm\\:subcase").trigger("click");
            $scope.submit();
            clearInterval(timer);
        }
    };
    $scope.beginCount = function () {
        var remaining = computeRemainingTime($scope.examCase.data.examination.timeLen, $scope.examCase.data.timePassed);
        passtime = $scope.examCase.data.examination.timeLen * 60 - remaining;
        maxtime = remaining;
        //console.log("remaining:"+$scope.examCase.data.examination.timeLen+",passed:"+$scope.examCase.data.timePassed);
        if ((timer === null)) {
            timer = setInterval(function () {
                $scope.countDown(maxtime, passtime);
            }, 1000);
        }
    };
});
function computeRemainingTime(timeLen, timePassed) {
    var len = timeLen * 60; //
    var remainingTime = len - timePassed;
    //console.log(remainingTime);
    return remainingTime;
}



