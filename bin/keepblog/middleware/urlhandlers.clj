(ns keepblog.middleware.urlhandlers
  (:use [compojure.core :only (defroutes)])
  (:require [keepblog.controllers.index :as index]
            [keepblog.controllers.user :as user]))

(defroutes app-routes
  index/routes
  user/routes)

