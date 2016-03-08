(ns keepblog.views.article
  (:require [keepblog.views.config :as config]))

; 使用 selmer 模板库
(use 'selmer.parser)
; 设置参数
(config/set-config)

(defn create-article 
  ([info] (render-file "article/create_article.html" info)))

(defn create-article-suc
  ([info] (render-file "article/create_article_suc.html" info)))

(defn show-article
  ([info] (render-file "article/show_article.html" info)))