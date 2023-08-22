## 👍 NumberPickerView

the NumberPicker view support multple number pick, two way bind with edittext and play sound when scroll.
![screen](https://github.com/TSIOJeft/NumberPickerView/blob/master/screen/screen.gif) 🤯gif not show sound.

# 😊 Binding with Edittext

```java
multiplepickerview.bindEditText(edittext)
```
and when you scroll number wheel the input will change and you change the input the wheel will turn.

# 🤩 Auto Increase

auto add picker when the input integer part length increase. 

# 😈 ShortComing

decimal part only support `.1f ` .

# 😂 How to use

```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

dependencies {
	        implementation 'com.github.TSIOJeft:NumberPickerView:v1.0.7'
	}


<com.farplace.numberpickerview.ui.MultipleNumberPickerView/>

```
# 😎 Custom

if you want other style with the picker maybe you should edit the project anyway.

(the turn sound from internet)
