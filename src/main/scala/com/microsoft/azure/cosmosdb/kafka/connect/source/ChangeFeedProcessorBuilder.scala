package com.microsoft.azure.cosmosdb.kafka.connect.source

import org.apache.kafka.connect.errors.ConnectException

import com.microsoft.azure.cosmosdb.kafka.connect.common.ErrorHandler.HandleRetriableError

import scala.reflect._


class ChangeFeedProcessorBuilder(feedCollectionInfo: DocumentCollectionInfo, leaseCollectionInfo: DocumentCollectionInfo, changeFeedProcessorOptions: ChangeFeedProcessorOptions, changeFeedObserver: ChangeFeedObserver)extends HandleRetriableError {

  def this() = this(null, null, new ChangeFeedProcessorOptions(), null)

  def withFeedCollection(newFeedCollectionInfo: DocumentCollectionInfo): ChangeFeedProcessorBuilder = {
    guardAgainstNull(newFeedCollectionInfo)
    return new ChangeFeedProcessorBuilder(newFeedCollectionInfo, this.leaseCollectionInfo, this.changeFeedProcessorOptions, this.changeFeedObserver)
  }

  def withLeaseCollection(newLeaseCollectionInfo: DocumentCollectionInfo): ChangeFeedProcessorBuilder = {
    guardAgainstNull(newLeaseCollectionInfo)
    return new ChangeFeedProcessorBuilder(this.feedCollectionInfo, newLeaseCollectionInfo, this.changeFeedProcessorOptions, this.changeFeedObserver)
  }

  def withProcessorOptions(newChangeFeedProcessorOptions: ChangeFeedProcessorOptions): ChangeFeedProcessorBuilder = {
    guardAgainstNull(newChangeFeedProcessorOptions)
    return new ChangeFeedProcessorBuilder(this.feedCollectionInfo, this.leaseCollectionInfo, newChangeFeedProcessorOptions, this.changeFeedObserver)
  }

  def withObserver(newChangeFeedObserver: ChangeFeedObserver): ChangeFeedProcessorBuilder = {
    guardAgainstNull(newChangeFeedObserver)
    return new ChangeFeedProcessorBuilder(this.feedCollectionInfo, this.leaseCollectionInfo, this.changeFeedProcessorOptions, newChangeFeedObserver)
  }

  def build(): ChangeFeedProcessor = {
    guardAgainstNull(this.feedCollectionInfo)
    guardAgainstNull(this.leaseCollectionInfo)
    guardAgainstNull(this.changeFeedProcessorOptions)
    guardAgainstNull(this.changeFeedObserver)

    return new ChangeFeedProcessor(this.feedCollectionInfo, this.leaseCollectionInfo, this.changeFeedProcessorOptions, this.changeFeedObserver)
  }

  private def guardAgainstNull[T: ClassTag](objectToCheck: T): Unit = {
    try{
      val className = classTag[T].runtimeClass.getSimpleName()
      val messageIfNull = "%s can't be null!".format(className)
      if (objectToCheck == null) throw new NullPointerException(messageIfNull)

      logger.debug("%s Object initialized".format(className))
    }catch{
      case f: Throwable =>
        throw new ConnectException("%s can't be null!".format(classTag[T].runtimeClass.getSimpleName()), f)
    }

  }

}
