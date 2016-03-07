(ns keepblog.views.config)

; 使用 selmer 模板库
(use 'selmer.parser)

(defn set-config []
  (do
    ; 关闭缓存，使网页修改能立即显示
		(selmer.parser/cache-off!) 
    ; 设置模板路径为 resource/templates
		(selmer.parser/set-resource-path! (clojure.java.io/resource "templates"))))
 
