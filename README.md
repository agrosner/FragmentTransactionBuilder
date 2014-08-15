FragmentTransactionBuilder
==========================

This library wraps around FragmentTransactions in an elegant way.
<br />

### Prerequisites

1. This project uses android support-v4:20.+
2. This project currently compiles with API 20 and buildToolsVersion 20.0.0, hopefully there will be a way to use project level in the future. 
3. MinSDk is 9

### Including in Your Project

1. Clone this repo or skip
2. Go to your build.gradle file

```groovy

dependencies{
  // Maven coming soon!
  
  //or locally in the a folder named "Libraries"
  compile project(':Libraries:FragmentTransactionBuilder');
}

```

### Usage

This library supports nearly all Fragment transaction methods and handles the messy logic for you. By default if you specify the *Fragment* class it will construct it with the default constructor. Also it will create tag based on its ```getClass().getSimpleName()```. The default *Mode* is replace.

```java

  new FragmentTransactionBuilder()
    .fragment(FragmentClass.class)
    .layoutId(R.id.SomeLayout)
    .mode(Mode.ADD)
    .execute(fragmentActivity);
    
  new FragmentTransactionBuilder()
    .fragment(FragmentClass.class)
    .layoutId(R.id.SomeLayout)
    .popBackStack(true)
    .execute(fragmentActivity);

```



### License

The MIT License (MIT)

Copyright (c) 2014 Andrew Grosner

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
