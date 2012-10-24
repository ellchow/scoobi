/**
 * Copyright 2011,2012 National ICT Australia Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nicta.scoobi
package core

import impl.plan._
import comp.CompNode
import WireFormat._

/* A wrapper around an object that is part of the graph of a distributed computation.*/
trait DObject[A] {
  implicit def mwf: ManifestWireFormat[A]
  implicit def mf = mwf.mf
  implicit def wf = mwf.wf

  private[scoobi]
  def getComp: CompNode

  /**Create a new distributed object by apply a function to this distributed object. */
  def map[B : ManifestWireFormat](f: A => B): DObject[B]

  /**Create a new distributed list by replicating the value of this distributed object
   * to every element within the provided distributed list. */
  def join[B : ManifestWireFormat](list: DList[B]): DList[(A, B)]
  def join[B : ManifestWireFormat](o: DObject[B]): DObject[(A, B)]

  def toSingleElementDList: DList[A] = (this join DList(())).map(_._1)
  def toDList[B](implicit ev: A <:< Iterable[B], mwfb: ManifestWireFormat[B]): DList[B] = toSingleElementDList.flatMap(x => x)
}

/** This object provides a set of operations to create distributed objects. */
object DObject extends DObjects {

  /** Create a new distributed list object from an "ordinary" value. */
  def apply[A : ManifestWireFormat](x: A): DObject[A] = DObjectImpl(x)
}

/** Implicit conversions for DObjects */
trait DObjects {
  /* Implicit conversions from tuples of DObjects to DObject tuples. */
  implicit def tupled2[T1 : ManifestWireFormat,
  T2 : ManifestWireFormat]
  (tup: (DObject[T1], DObject[T2])): DObject[(T1, T2)] = DObjectImpl.tupled2(tup)

  implicit def tupled3[T1 : ManifestWireFormat,
  T2 : ManifestWireFormat,
  T3 : ManifestWireFormat]
  (tup: (DObject[T1], DObject[T2], DObject[T3])): DObject[(T1, T2, T3)] =
    tupled2(tup._1, tupled2(tup._2, tup._3)) map { case (a, (b, c)) => (a, b, c) }

  implicit def tupled4[T1 : ManifestWireFormat,
  T2 : ManifestWireFormat,
  T3 : ManifestWireFormat,
  T4 : ManifestWireFormat]
  (tup: (DObject[T1], DObject[T2], DObject[T3], DObject[T4])): DObject[(T1, T2, T3, T4)] =
    tupled2(tup._1, tupled3(tup._2, tup._3, tup._4)) map { case (a, (b, c, d)) => (a, b, c, d) }

  implicit def tupled5[T1 : ManifestWireFormat,
  T2 : ManifestWireFormat,
  T3 : ManifestWireFormat,
  T4 : ManifestWireFormat,
  T5 : ManifestWireFormat]
  (tup: (DObject[T1], DObject[T2], DObject[T3], DObject[T4], DObject[T5])): DObject[(T1, T2, T3, T4, T5)] =
    tupled2(tup._1, tupled4(tup._2, tup._3, tup._4, tup._5)) map { case (a, (b, c, d, e)) => (a, b, c, d, e) }

  implicit def tupled6[T1 : ManifestWireFormat,
  T2 : ManifestWireFormat,
  T3 : ManifestWireFormat,
  T4 : ManifestWireFormat,
  T5 : ManifestWireFormat,
  T6 : ManifestWireFormat]
  (tup: (DObject[T1], DObject[T2], DObject[T3], DObject[T4], DObject[T5], DObject[T6])): DObject[(T1, T2, T3, T4, T5, T6)] =
    tupled2(tup._1, tupled5(tup._2, tup._3, tup._4, tup._5, tup._6)) map { case (a, (b, c, d, e, f)) => (a, b, c, d, e, f) }

  implicit def tupled7[T1 : ManifestWireFormat,
  T2 : ManifestWireFormat,
  T3 : ManifestWireFormat,
  T4 : ManifestWireFormat,
  T5 : ManifestWireFormat,
  T6 : ManifestWireFormat,
  T7 : ManifestWireFormat]
  (tup: (DObject[T1], DObject[T2], DObject[T3], DObject[T4], DObject[T5], DObject[T6], DObject[T7])): DObject[(T1, T2, T3, T4, T5, T6, T7)] =
    tupled2(tup._1, tupled6(tup._2, tup._3, tup._4, tup._5, tup._6, tup._7)) map { case (a, (b, c, d, e, f, g)) => (a, b, c, d, e, f, g) }

  implicit def tupled8[T1 : ManifestWireFormat,
  T2 : ManifestWireFormat,
  T3 : ManifestWireFormat,
  T4 : ManifestWireFormat,
  T5 : ManifestWireFormat,
  T6 : ManifestWireFormat,
  T7 : ManifestWireFormat,
  T8 : ManifestWireFormat]
  (tup: (DObject[T1], DObject[T2], DObject[T3], DObject[T4], DObject[T5], DObject[T6], DObject[T7], DObject[T8])): DObject[(T1, T2, T3, T4, T5, T6, T7, T8)] =
    tupled2(tup._1, tupled7(tup._2, tup._3, tup._4, tup._5, tup._6, tup._7, tup._8)) map { case (a, (b, c, d, e, f, g, h)) => (a, b, c, d, e, f, g, h) }
}
