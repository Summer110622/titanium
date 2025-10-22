# 設計書 (build.md)

## 1. システムアーキテクチャ

本システムは、責務ごとに分割されたマルチモジュールアーキテクチャを採用する。各モジュールは独立して開発・テストが可能であり、システム全体の保守性と拡張性を高める。CLI（Command Line Interface）をエントリーポイントとし、各機能モジュールを呼び出す構成とする。

## 2. モジュール設計

- **`cli` (Command Line Interface Module)**
  - 責務: ユーザーからのコマンドを受け付け、適切なモジュールに処理を委譲する。
  - 機能: コマンドの解析、ヘルプメッセージの表示。
  - 使用技術: Picocli (Java) などを想定。

- **`patcher` (Patching Module)**
  - 責務: PaperMCのソースコードに対するパッチの適用・管理を行う。
  - 機能: 特定バージョンのPaperMCソースの取得、パッチファイルの適用、パッチ適用状態の管理。

- **`customizer` (Customization Module)**
  - 責務: サーバーのカスタマイズ機能を提供する。
  - 機能: サーバー名の変更、カスタムAPI/ハンドラのビルドと統合。

- **`performance` (Performance Measurement Module)**
  - 責務: サーバーのパフォーマンス測定を行う。
  - 機能: TPS、メモリ使用量などのメトリクスを収集・記録し、レポートを生成する。

- **`optimizer` (Optimization Module)**
  - 責務: オーバーライドによる最適化機能を提供する。
  - 機能: 設定ファイルに基づき、特定のクラスやメソッドをオーバーライドまたは無効化する。

## 3. ディレクトリ構造

```
titanium/
├── cli/                      # CLIモジュール
│   ├── src/
│   └── build.gradle
├── patcher/                  # Patcherモジュール
│   ├── src/
│   └── build.gradle
├── customizer/               # Customizerモジュール
│   ├── src/
│   └── build.gradle
├── performance/              # Performanceモジュール
│   ├── src/
│   └── build.gradle
├── optimizer/                # Optimizerモジュール
│   ├── src/
│   └── build.gradle
├── build.gradle              # ルートのビルドスクリプト
└── settings.gradle           # モジュール定義
```

## 4. 使用技術

- **言語**: Java 17 or later
- **ビルドツール**: Gradle
- **CLIライブラリ**: Picocli

## 5. データフロー（パッチ適用の場合）

1. ユーザーが `titanium patch <version>` コマンドを実行する。
2. `cli`モジュールがコマンドを解析し、`patcher`モジュールを呼び出す。
3. `patcher`モジュールが指定されたバージョンのPaperMCソースをダウンロードする。
4. 対応するパッチを取得し、ソースコードに適用する。
5. 結果（成功/失敗）を`cli`モジュールに返し、ユーザーに表示する。
