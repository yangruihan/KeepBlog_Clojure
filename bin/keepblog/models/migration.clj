(ns keepblog.models.migration
  (:require [clojure.java.jdbc :as sql]))

;; 定义数据库连接 url
(def spec 
  (or (System/getenv "DATABASE_URL")
      "mysql:///keepblog?useUnicode=true&characterEncoding=UTF-8&user=root&password=123456"))

(defn- migrated? [table-name]
  (-> (sql/query spec
                 [(str "SELECT COUNT(*) AS count FROM information_schema.tables "
                       "where table_name='" table-name "'")])
    first :count pos?))

(def user-table-name "users")
(def user-info-table-name "user_infos")
(def tag-table-name "tags")
(def category-table-name "categories")
(def article-table-name "articles")
(def tag-article-table-name "tag_article")

(defmacro create-table [table-name & parameters]
  `(when (not (migrated? ~table-name))
     (println (str "Creating table " ~table-name "...")) (flush)
     (sql/db-do-commands spec
                         (sql/create-table-ddl
                           ~table-name
                           ~@parameters))
     (println "Create Done!")))

(defn- create-user-table []
  (create-table user-table-name 
                [:id "int NOT NULL PRIMARY KEY AUTO_INCREMENT"]
                [:username "varchar(50) NOT NULL"]
                [:password "varchar(50) NOT NULL"]
                [:email "varchar(255) NOT NULL"]
                [:create_time "timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP"]))

(defn- create-user-info-table []
  (create-table user-info-table-name
                [:id "int NOT NULL PRIMARY KEY AUTO_INCREMENT"]
                [:nickname "varchar(50) NOT NULL"]
                [:realname "varchar(50) NULL"]
                [:birthday "datetime NULL"]
                [:area "varchar(100) NULL"]
                [:description "text NULL"]
                [:user_id "int NOT NULL"]
                ["CONSTRAINT `fk_userinfo_to_user` FOREIGN KEY (`user_id`) REFERENCES" user-table-name "(`id`) ON DELETE CASCADE ON UPDATE CASCADE"]))

(defn- create-tag-table []
  (create-table tag-table-name
                [:id "int NOT NULL PRIMARY KEY AUTO_INCREMENT"]
                [:tagname "varchar(64) NOT NULL"]))

(defn- create-category-table []
  (create-table category-table-name
                [:id "int NOT NULL PRIMARY KEY AUTO_INCREMENT"]
                [:categoryname "varchar(64) NOT NULL"]
                [:user_id "int NOT NULL"]
                ["CONSTRAINT `fk_category_to_user` FOREIGN KEY (`user_id`) REFERENCES" user-table-name "(`id`) ON DELETE CASCADE ON UPDATE CASCADE"]))

(defn- create-article-table []
  (create-table article-table-name
                [:id "int NOT NULL PRIMARY KEY AUTO_INCREMENT"]
                [:title "varchar(100) NOT NULL"]
                [:create_time "timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP"]
                [:user_id "int NOT NULL"]
                [:category_id "int NOT NULL"]
                [:content "text NULL"]
                ["CONSTRAINT `fk_article_to_user` FOREIGN KEY (`user_id`) REFERENCES" user-table-name "(`id`) ON DELETE CASCADE ON UPDATE CASCADE"]
                ["CONSTRAINT `fk_article_to_category` FOREIGN KEY (`category_id`) REFERENCES" category-table-name "(`id`) ON UPDATE CASCADE"]))

(defn- create-tag-article-table []
  (create-table tag-article-table-name
                [:id "int NOT NULL PRIMARY KEY AUTO_INCREMENT"]
                [:tag_id "int NOT NULL"]
                [:article_id "int NOT NULL"]
                ["CONSTRAINT `fk_tag_article_to_tag` FOREIGN KEY (`tag_id`) REFERENCES" tag-table-name "(`id`) ON DELETE CASCADE ON UPDATE CASCADE"]
                ["CONSTRAINT `fk_tag_article_to_article` FOREIGN KEY (`article_id`) REFERENCES" article-table-name "(`id`) ON DELETE CASCADE ON UPDATE CASCADE"]))
                
(defn migrate []
  (do
    (create-user-table)
    (create-user-info-table)
    (create-tag-table)
    (create-category-table)
    (create-article-table)
    (create-tag-article-table)))
                          