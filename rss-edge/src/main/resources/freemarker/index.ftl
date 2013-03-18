<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="refresh">
    <title>Netflix OSS RSS Reader</title>

    <!-- TODO: Should host locally -->
    <link href="http://netdna.bootstrapcdn.com/twitter-bootstrap/2.2.1/css/bootstrap.css" rel="stylesheet">
    <style type="text/css">
        body {
            padding-top: 60px;
            padding-bottom: 40px;
        }
    </style>
    <link href="http://netdna.bootstrapcdn.com/twitter-bootstrap/2.2.1/css/bootstrap.min.css" rel="stylesheet">
</head>

<body>

<div class="navbar navbar-inverse navbar-fixed-top">
    <div class="navbar-inner">
        <div class="container">
            <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="brand" href="/">Netflix OSS RSS Reader</a>

            <div class="nav-collapse collapse">
                <form class="navbar-form pull-right" method="POST" action="/">
                    <input class="span8" type="text" placeholder="Enter the feed Url" name="url">
                    <button type="submit" class="btn">Add</button>
                </form>
            </div>
            <!--/.nav-collapse -->
        </div>
    </div>
</div>

<!-- To hide/show the delete button -->
<style type="text/css">
    h4 .icon-remove {
        visibility:hidden;
    }
    h4:hover .icon-remove {
        visibility:visible;
    }
</style>

<div class="container">
<#assign itemIndex=1>
<#list subscriptions?chunk(3) as row>
    <div class="row">
    <#list row as rss>
        <div class="span4">
            <h4>${rss.title} <a href="?delFeedUrl=${rss.url?url("UTF8")}"><i class="icon-remove"></i> </a></h4>
            <#list rss.items as item>
                <#if item_index gte 4><#break></#if>
                <#if item.description?length lt 800>
                    <#assign len=item.description?length>
                <#else>
                    <#assign len=800>
                </#if>
                <p id="item${itemIndex}" data-container="body"
                   data-content="${item.description?replace("\"", "'")?substring(0, len)}"
                   data-trigger="hover" data-placement="right" data-html="true">
                    <a href="${item.link}">${item.title}</a></p>
                <#assign itemIndex=itemIndex+1>
            </#list>
        </div>
    </#list>
    </div>
</#list>
</div>


<hr>
<footer align="center">
    <p>Netflix Inc. 2013</p>
</footer>
</div> <!-- /container -->

<!-- Le javascript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="http://code.jquery.com/jquery.js"></script>
<script src="http://netdna.bootstrapcdn.com/twitter-bootstrap/2.2.1/js/bootstrap.js"></script>
<script src="http://netdna.bootstrapcdn.com/twitter-bootstrap/2.2.1/js/bootstrap.min.js"></script>
<script>
    $(function ()
    {
    <#list 1..itemIndex as i>
        $("#item${i}").popover();
    </#list>
    });
</script>
</body>
</html>