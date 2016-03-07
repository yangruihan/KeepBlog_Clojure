(ns keepblog.models.korma-config
  (:use [korma.db :refer [defdb mysql]]
        [korma.core :exclude [update]])
  (:require [clojure.string :as string]))

(defdb db
  (mysql {:db "keepblog",
          :host "localhost",
          :port 3306,
          :user "root",
          :password "123456"}))

(declare users user_infos tags tag_article categories articles)

(defentity users
  (has-one user_infos {:fk :user_id})
  (has-many articles {:fk :user_id}))
    

(defentity user_infos
  (belongs-to users {:fk :user_id}))

