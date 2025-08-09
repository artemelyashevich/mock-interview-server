package com.mock.interview.lib.entity;

public abstract class AbstractEntity implements Cloneable {

  @Override
  public AbstractEntity clone() {
    try {
      return (AbstractEntity) super.clone();
    } catch (CloneNotSupportedException e) {
      throw new AssertionError();
    }
  }
}
