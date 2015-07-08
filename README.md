# meeting-burn

Shows how much money is being spent during a meeting

## Development Mode

```
lein clean
lein figwheel
```

[Figwheel](https://github.com/bhauman/lein-figwheel) will automatically push cljs changes to the browser.

Wait a bit, then browse to [http://localhost:3449](http://localhost:3449).

## Production Build

```
lein clean
lein cljsbuild once min
```
