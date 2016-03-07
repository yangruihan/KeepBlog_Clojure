(ns keepblog.controllers.index
  (:require [compojure.core :refer [defroutes GET POST]]
            [clojure.string :as str]
            [ring.util.response :as ring]
            [keepblog.views.index :as view]))

;; 主页
(defn index []
  (view/index))

;; 路由设置
(defroutes routes
  (GET "/" [] (index)))

