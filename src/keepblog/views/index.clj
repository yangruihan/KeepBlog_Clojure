(ns keepblog.views.index)

; 使用 selmer 模板库
(use 'selmer.parser)

; 关闭缓存，使网页修改能立即显示
(selmer.parser/cache-off!)

; 设置模板路径为 resource/templates
(selmer.parser/set-resource-path! (clojure.java.io/resource "templates"))

(defn index []
  (render-file "index.html" {}))