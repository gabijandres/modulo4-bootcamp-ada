(ns exercises.class4.main
  (:require [schema.core :as s]))

; Ex 1
(s/defrecord Aluno
  [id :- s/Int
   nome :- s/Str])

(s/defrecord Professor
  [id :- s/Int
   nome :- s/Str])

(s/defrecord Turma
  [id :- s/Int
   alunos :- [s/Int]
   nome :- s/Str])

; Tests
(comment
  (s/validate Aluno (Aluno. 1 "Victor"))
  (s/validate Aluno (Aluno. 1 1)) ; error case
  (s/validate Professor (Professor. 1 "Victor"))
  (s/validate Turma (Turma. 1 [1, 2, 3] "968")))

; Ex 2

(defprotocol RegistrarPresenca
  (registrar [this id-aluno id-turma data]))

(s/defrecord Presenca
  [id-aluno :- s/Int
   id-turma :- s/Int
   data :- s/Str
   presente :- s/Bool]
  RegistrarPresenca
  (registrar [this id-aluno id-turma data]
    (Presenca. id-aluno id-turma data true)))

; Tests
(comment
  (s/validate Presenca (Presenca. 1 1 "05/06/2023" true))
  (def P (Presenca. 1 1 "05/06/2023" true))
  (registrar P 1 1 "05/06/2023"))

